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
	
}
