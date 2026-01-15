package kr.co.noir.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("/main")
	public String Main() {
		return "/main";
	}

	@GetMapping("/memberLogin")
	public String memberLogin() {
		return "/login/memberLogin";
	}
	
	
	@GetMapping("/adminLogin")
	public String adminLogin() {
		return "/login/adminLogin";
	}
	
	@GetMapping("/admin")
	public String admin() {
		return "admin_main/adminMain";
	}
	
	@GetMapping("/admin/dinningMgr")
	public String dinningMgr() {
		return "admin_dinning/dinningManage";
	}
	@GetMapping("/admin/adminRoom")
	public String admin_room() {
		return "admin_room/roomManagePrice";
	}
	
	@GetMapping("/room")
	public String Room() {
		return "/room/room";
	}
	@GetMapping("/roomDetail")
	public String RoomDetail() {
		return "/room/roomDetail";
	}
	
	@GetMapping("/dinning")
	public String Dinning() {
		return "/dinning/dinning";
	}
	@GetMapping("/dinningDetail")
	public String DinningDetail() {
		return "/dinning/dinningDetail";
	}
}
