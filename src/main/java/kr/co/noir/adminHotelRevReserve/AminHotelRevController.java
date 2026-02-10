package kr.co.noir.adminHotelRevReserve;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping ("/admin/hotelMember")
@Controller
public class AminHotelRevController {

	@GetMapping("/HotelRev")
	public String adminHotelMemberList (int reserveNum,Model model) {
		
		
		
		
		
		return "/manager/memberReserve/hotelRev";
	}//hotelMemberDetail
	
	
	@GetMapping("/Hoteldetail")
	public String adminHotelMemberDetail () {		
		
		return "/manager/memberReserve/hotelRevDetail";
	}//hotelMemberDetail
	
	
	
}//class
