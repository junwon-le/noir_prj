package kr.co.noir.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("/main")
	public String Main() {
		return "/main";
	}
	
	@GetMapping("/adminLogin")
	public String adminLogin() {
		return "/login/adminLogin";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "manager/main/adminMain";
	}
	
	@GetMapping("/admin/dinningMgr")
	public String dinningMgr() {
		return "manager/dinning/dinningManage";
	}
	@GetMapping("/admin/adminRoom")
	public String admin_room() {
		return "manager/room/roomManagePrice";
	}
	


	
	/*
	 @GetMapping("/dinning") 
	 public String Dinning() { 
		  return "/dinning/dinning";
	  }
	
	@GetMapping("/dinningDetail")
	public String DinningDetail() {
		return "/dinning/dinningDetail";
	}
	
	@GetMapping("/hotelRev")
	public String hotelRev() {
		return "/manager/hotel_rev/hotelRev";
	}
	
	@GetMapping("/hotelRevDetail")
	public String hotelRevDetail() {
		return "/manager/hotel_rev/hotelRev_detail";
	}
	@GetMapping("/dinningRev")
	public String dinningRev() {
		return "/manager/dinning_rev/dinningRev";
	}
	
	@GetMapping("/dinningRevDetail")
	public String dinningRevDetail() {
		return "/manager/dinning_rev/dinningRev_detail";
	}
*/
}
