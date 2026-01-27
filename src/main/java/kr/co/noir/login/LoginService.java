package kr.co.noir.login;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {

	@Autowired(required = false)
	private LoginDAO lDAO;
	private final MemberMapper memberMapper;
	
	@Value("${user.crypto.key}")
	private String key;
	@Value("${user.crypto.salt}")
	private String salt;	
	
	
	public LoginMemberDomain searchOneMember(LoginDTO lDTO) throws PersistenceException {
		LoginMemberDomain md=null;
		
		try {
			md=lDAO.selectOneMember(lDTO.getMemberId()); //아이디를 넣어서 이름, 이메일, 비밀번호 검색

			if( md==null) { //아이디가 존재하지 않음.
				md=new LoginMemberDomain();
			   	md.setResultMsg("아이디가 존재하지 않습니다.");
			}else { //아이디는 존재함
				// 비밀번호 검색 : BcryptPasswordEncorder 사용. 
				// 암호화된 자료 비교는 matches로만 해야 한다.
				// 암호화된 경우에만 암호화 하여 비교한다. 암호화된 자료는 항상60자리
				if (md.getMemberPass().length()==60) {
					BCryptPasswordEncoder bce=new BCryptPasswordEncoder();
				 
					if(bce.matches(lDTO.getMemberPass(), md.getMemberPass())) {
						//비번 일치
						md.setResultMsg("로그인 성공");
						lDTO.setResult("S");
						
					}else {
						//비번 불일치
						md.setResultMsg("로그인 실패");
						lDTO.setResult("F");
					}
					
				}else { //암호화 된 자료가 아니면 
						if(lDTO.getMemberPass().equals(md.getMemberPass())) {
							//비번 일치
							md.setResultMsg("로그인 성공");
							lDTO.setResult("S");
							
						}else {
							//비번 불일치
							md.setResultMsg("로그인 실패");
							lDTO.setResult("F");
						}
				}
				
			}
			
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}//end catch
		
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
                                            String memberFirstName, String memberEmail) {
        
        // 4가지 조건이 모두 일치하는 데이터가 있는지 조회
        int count = memberMapper.checkUserForPasswordReset(memberId, memberLastName, memberFirstName, memberEmail);
        
        // 결과가 1이면 일치하는 회원이 있는 것으로 판단하여 true 반환
        return count == 1;
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
        
		// 3. Password 암호화 일방향 해시 : 비번
		BCryptPasswordEncoder bpe=new BCryptPasswordEncoder(10);
		memberDTO.setMemberPass(bpe.encode(memberDTO.getMemberPass()));
		
		//암호화 : 이름, 이메일, - 컬럼 크기가 작아서 암호화 하면 오류 발생 
		
		// 4. DB Insert 실행
		try {
			result = memberMapper.insertMember(memberDTO);
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}//end catch

		// 
        return result == 1;
    }    
    
}//class
