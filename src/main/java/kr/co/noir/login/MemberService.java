package kr.co.noir.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MemberService {

	@Autowired
	private MemberMapper memberMapper;
	
	public MemberDTO login(String memberId, String memberPass) {
		return memberMapper.login(memberId, memberPass);
	}//login
	
	public MemberDTO findIdByNameAndEmail(String memberName, String memberEmail) {
		MemberDTO foundId=null;
		foundId= memberMapper.findIdByNameAndEmail(memberName, memberEmail);
		return foundId;
	}//findIdByNameAndEmail

	
    @Transactional
    public boolean modifyPassword(String userId, String newPw) {
        // 비밀번호 변경 실행 (영향을 받은 행의 수가 1이면 성공)
        return memberMapper.updatePassword(userId, newPw) == 1;
    }
	
}//class



