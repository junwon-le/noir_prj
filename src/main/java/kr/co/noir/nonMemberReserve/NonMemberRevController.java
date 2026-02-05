package kr.co.noir.nonMemberReserve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/nonMember")
@Controller
public class NonMemberRevController {
	
	@Autowired
	private NonMemberRevService nmrs;
	
	
	@PostMapping("/reserveCheck")
	public String reserveCheck(NonMemberRevDTO nmrDTO) {
		
		System.out.println("이메일"+nmrDTO.getEmail());
		System.out.println("비밀번호"+nmrDTO.getPassword());
		System.out.println("예약번호"+nmrDTO.getReserveNum());
		System.out.println("타입"+nmrDTO.getReserveType());
		
		System.out.println("비밀번호 결과 : " +nmrs.NonReserveCheck(nmrDTO));
		return "/mypage/main";
		
	}//reserveCheck
}//class
