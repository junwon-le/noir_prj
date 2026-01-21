package kr.co.noir.mypage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.co.noir.login.MemberDTO;

@RequestMapping("/mypage")
@Controller
public class MypageController {


	@Autowired
	MypageService mps;


	@GetMapping("/")
	public String mypageView(HttpSession session, Model model) {
		String uri="redirect:/main";
		
		MemberDTO member= (MemberDTO)session.getAttribute("member");
		if(member != null&&member.getMemberId()!=null) {

			String memberId=member.getMemberId();
			
			int cnt =mps.searchHotelRevCnt(memberId);
			System.out.println(cnt);
			model.addAttribute("hotelRevCnt", cnt);
			uri="/mypage/main";
		}//end if
		
		
		
		
		return uri;
	}//mypageView
	
	
}//class
