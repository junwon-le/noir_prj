package kr.co.noir.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;


@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@GetMapping("/memberLogin")
	public String login() {
		return "memberService";
	}
	
	@PostMapping("/admin/login")
	public String loginProcess(@RequestParam("memberId") String memberId, @RequestParam("memberPass") String memberPass, HttpSession session) {
		
		MemberDTO member = memberService.login(memberId, memberId);
		
		if (member != null) {
			// 로그인 성공: 세션에 관리자 정보 저장
			session.setAttribute("member", member);
			return "redirect:/main"; // 첫화면으로 
		} else {
			// 로그인 실패: 에러 파라미터와 함께 로그인 페이지로 리다이렉트
			return "redirect:/main/login?error=true";
		}
	}
}