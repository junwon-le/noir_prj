 package kr.co.noir.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;


@Controller
public class AdminController {

	@Autowired
	private AdminService adminService;
	
	@GetMapping("/admin/login")
	public String login() {
		return "adminLogin";
	}

	@GetMapping("/admin/dashboard")
	public String dashboard() {
		return "adminDashboard";
	}
	
	@PostMapping("/admin/login")
	public String loginProcess(@RequestParam("adminId") String adminId,
			@RequestParam("adminPass") String adminPass,
			HttpSession session) {
		
		AdminDTO admin = adminService.login(adminId, adminPass);
		System.out.println(adminId +" / " + adminPass +" / " +  admin);
		
		if (admin != null) {
			
			System.out.println("admin 로그인 성공");
			// 로그인 성공: 세션에 관리자 정보 저장
			session.setAttribute("admin", admin);
			return "redirect:/main"; // 대시보드 페이지로 리다이렉트
		} else {
			// 로그인 실패: 에러 파라미터와 함께 로그인 페이지로 리다이렉트
			System.out.println("admin 로그인 실패");
			return "redirect:/main/adminLogin?error=true";
		}
	}
}