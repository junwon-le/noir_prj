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
	
	
	@GetMapping("/hotel/about")
	public String aboutHotel() {
		return "/hotel/about";
	}
	
	@GetMapping("/hotel/library")
	public String hotelLibrary() {
		return "/hotel/library";
	}

	@GetMapping("/hotel/gym")
	public String hotelGym() {
		return "/hotel/gym";
	}	

	@GetMapping("/hotel/laundry")
	public String hotelLaundry() {
		return "/hotel/laundry";
	}	
	
	@GetMapping("/hotel/location")
	public String location() {
		return "/hotel/location";
	}	

	@GetMapping("/join")
	public String join() {
		return "/login/join";
	}
	@GetMapping("/noticeAdmin")
	public String notice() {
		return "manager/notice/noticeAdmin";
	}
	@GetMapping("/noticeWriteAdmin")
	public String noticeWriteAdmin() {
		return "manager/notice/noticeWriteFrmAdmin";
	}

}
