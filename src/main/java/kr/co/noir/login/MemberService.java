package kr.co.noir.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
	
}//class



