package kr.co.noir.nonMemberReserve;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/nonMember")
@Controller
public class NonMemberRevController {
	
	@Autowired
	private NonMemberRevService nmrs;
	
	
	@PostMapping("/reserveCheck")
	public String reserveCheck(NonMemberRevDTO nmrDTO,Model model) {
	
		boolean reserveFlag = true;
		String reserveType=nmrDTO.getReserveType();
		String uri = "/login/memberLogin";
		
		
		System.out.println("이메일"+nmrDTO.getEmail());
		System.out.println("비밀번호"+nmrDTO.getPassword());
		System.out.println("예약번호"+nmrDTO.getReserveNum());
		
		reserveFlag=nmrs.NonReserveCheck(nmrDTO);
		System.out.println("확인결과"+reserveType);
		System.out.println("확인결과"+reserveFlag);
		if(reserveFlag) {//받은 예약들이 존재하고 비밀번호가 맞는 경우 
			if("room".equals(reserveType)) {//reserveType이 room인 경우
				
				//roomDetailServic 넣기
				//model로해서 값보내기
				
				model.addAttribute("hotelRevDetail",  nmrs.searchOneHotelRevDetail(nmrDTO));
				uri="/nonReserve/nonMemberHotelRevDetail";
			}else {
				
				//dinningDetailServic 넣기
				//model로해서 값보내기
				model.addAttribute("dinningRevDetail",  nmrs.searchOneDinningRevDetail(nmrDTO));
				
				uri="/nonReserve/nonMemberDinningDetail";
			}//end else
				
		}//end if 
		
		/* System.out.println("비밀번호 결과 : " +); */
		
		
		model.addAttribute("reserveFlag", !reserveFlag);
		
		
		return 	uri;
		
	}//reserveCheck
}//class
