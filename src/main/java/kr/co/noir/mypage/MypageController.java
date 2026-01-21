package kr.co.noir.mypage;
import kr.co.noir.NoirPrjApplication;
import kr.co.noir.login.MemberDTO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/mypage")
@Controller
public class MypageController {




	@GetMapping("/")
	public String mypageView(HttpSession session, Model model) {
		String uri="redirect:/main";
		if(session!=null) {
			
			MemberDTO member= (MemberDTO)session.getAttribute("member");
			System.out.println(member.getMemberId());
			
			uri="/mypage/main";
		}
		return uri;
	}//mypageView
	
	
}//class
