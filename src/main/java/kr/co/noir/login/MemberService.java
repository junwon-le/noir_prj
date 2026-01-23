package kr.co.noir.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class MemberService {

	@Autowired
	private MemberMapper memberMapper;
	
	public MemberDTO login(String memberId, String memberPass) {
		return memberMapper.login(memberId, memberPass);
	}//login
	
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
        return memberMapper.updatePassword(memberId, newPw) == 1;
    }

    
    public boolean existsById(String memberId) {
    		return memberMapper.selectIdCount(memberId) > 0;
    }//existsById
    
    
    /**
     * 회원가입 처리 (성/이름 개별 저장 반영)
     */
    @Transactional
    public boolean registerMember(MemberDTO memberDTO, HttpServletRequest request) {
        // 1. 가입자 IP 설정 (이미지의 MEMBER_IP 컬럼 반영)
        memberDTO.setMemberIp(request.getRemoteAddr());
        
        // 2. 가입 시 기본값 설정 (이미지의 MEMBER_DEL_FLAG 등)
        memberDTO.setMemberDelFlag("N");
        
        // 3. DB Insert 실행
        int result = memberMapper.insertMember(memberDTO);

        return result == 1;
    }    
    
}//class
