package kr.co.noir.nonUserReserve;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/nonuser")
@Controller
public class NonUserContriller {
	
	@GetMapping("/dinningList")
	public String DinningListView() {
	
		return "/nonReserve/nonMemberDinningList";
	}//DinningListView

	@GetMapping("dinningDetail")
	public String DinningRevDetail() {	
		return "/nonReserve/nonMemberDinningDetail";
	}//DinningRevDetail
	
	
	@GetMapping("/hotelList")
	public String HotelListView() {
		
		
		return "/nonReserve/nonMemberHotelList";
	}//HotelListView
	
	@GetMapping("/hotelDetail")
	public String HotelRevDetail() {
		
		return "/nonReserve/nonMemberHotelRevDetail";
	}//HotelRevDetail
	
	
}//class
