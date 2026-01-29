package kr.co.noir.login;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

	@Autowired(required = false)
	private final MemberMapper memberMapper;
	
	@Value("${user.crypto.key}")
	private String key;
	@Value("${user.crypto.salt}")
	private String salt;	
	
	// BCrypt 객체는 하나만 생성해서 재사용하는 것이 성능상 유리합니다.
    private final BCryptPasswordEncoder bce = new BCryptPasswordEncoder();

    public LoginMemberDomain searchOneMember(LoginDTO lDTO) {
        LoginMemberDomain md = null;
        
        try {
            // 1. DB에서 사용자 정보 조회
            md = memberMapper.selectOneUserInfo(lDTO.getMemberId());

            if (md == null) {
                // 2. 아이디가 존재하지 않는 경우
                md = new LoginMemberDomain();
                md.setResultMsg("아이디가 존재하지 않습니다.");
                lDTO.setResult("F");
            } else {
                // 3. 아이디가 존재하는 경우 비밀번호 검증
                String inputPw = lDTO.getMemberPass(); // 사용자가 입력한 비번
                String dbPw = md.getMemberPass();      // DB에 저장된 비번 (암호문 또는 평문)
                boolean isMatch = false;

                // 암호화된 데이터(BCrypt는 항상 60자)인지 확인
                if (dbPw != null && dbPw.length() == 60) {
                    isMatch = bce.matches(inputPw, dbPw);
                } else {
                    // 암호화되지 않은 평문 데이터 비교
                    isMatch = inputPw.equals(dbPw);
                }

                // 4. 검증 결과 세팅
                if (isMatch) {
                    md.setResultMsg("로그인 성공");
                    lDTO.setResult("S");
                } else {
                    md.setResultMsg("비밀번호가 일치하지 않습니다.");
                    lDTO.setResult("F");
                }
            }
        } catch (PersistenceException pe) {
            pe.printStackTrace();
            // 에러 발생 시 처리
            if(md == null) md = new LoginMemberDomain();
            md.setResultMsg("시스템 오류가 발생했습니다.");
            lDTO.setResult("E");
        }
        
        return md;
		
	}//searchOneMember
	
	
	
	public String findIdByInfo(String memberLastName, String memberFirstName, String memberEmail) {
		String foundId=null;
		foundId=memberMapper.findIdByInfo(memberLastName, memberFirstName, memberEmail);
		return foundId;
	}//findIdByInfo
	
	
	/**
     * 비밀번호 찾기 시 입력한 정보가 실제 DB 정보와 일치하는지 확인
     */
	public boolean checkUserForPasswordReset(String memberId, String memberLastName, 
            String memberFirstName, String inputEmail) {

		// 1. ID와 이름으로 DB 회원 정보 조회 (이메일 제외하고 조회)
		MemberDTO mDTO = memberMapper.checkUserForReset(memberId, memberLastName, memberFirstName);
		
		// 2. 회원이 없으면 false
		if (mDTO == null || mDTO.getMemberEmail() == null) {
		return false;
		}

		TextEncryptor te= Encryptors.text(key, salt);
		
		// 3. DB에 있는 암호문 가져오기
		String dbEncEmail = mDTO.getMemberEmail();
		
		// 4. 복호화 (DB값 -> 평문)
		String dbDecEmail = te.decrypt(dbEncEmail);
		
		System.out.println(dbEncEmail);
		
		System.out.println(inputEmail);
		System.out.println(dbDecEmail);
		
		// 5. 사용자가 입력한 이메일과 비교
		return inputEmail.equals(dbDecEmail);
		}
    
	
    @Transactional
    public boolean modifyPassword(String memberId, String newPw) {
        // 비밀번호 변경 실행 (영향을 받은 행의 수가 1이면 성공)

    	// 비밀번호 BcryptPasswordEncorder 사용. 
		BCryptPasswordEncoder bpe=new BCryptPasswordEncoder();
		String encPw=bpe.encode(newPw);
    	
        return memberMapper.updatePassword(memberId, encPw) == 1;
    }

    
    public boolean existsById(String memberId) {
    		return memberMapper.selectIdCount(memberId) > 0;
    }//existsById
    
    
    /**
     * 회원가입 처리 (성/이름 개별 저장 반영)
     */
    @Transactional
    public boolean registerMember(MemberDTO memberDTO, HttpServletRequest request) {

    	int result=0;
    	
    	// 1. 가입자 IP 설정 (이미지의 MEMBER_IP 컬럼 반영)
        memberDTO.setMemberIp(request.getRemoteAddr());
        
        // 2. 가입 시 기본값 설정 (이미지의 MEMBER_DEL_FLAG)
        memberDTO.setMemberDelFlag("N");
        
		// 3. Password 암호화 일방향 해시 : 비번, Bcrypt
		BCryptPasswordEncoder bpe=new BCryptPasswordEncoder(10);
		memberDTO.setMemberPass(bpe.encode(memberDTO.getMemberPass()));

		TextEncryptor te= Encryptors.text(key, salt);
		// 4. 이메일, 전화번호 
		if (memberDTO.getMemberEmail() != null) {
			memberDTO.setMemberEmail(te.encrypt(memberDTO.getMemberEmail()));
		}
		if (memberDTO.getMemberTel() != null) {
			memberDTO.setMemberTel(te.encrypt(memberDTO.getMemberTel()));
		}
		
		// 5. DB Insert 실행
		try {
			result = memberMapper.insertMember(memberDTO);
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}//end catch

		// 
        return result == 1;
    }    
    
}//class
