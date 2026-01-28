package kr.co.noir.mypage;
import java.util.List;

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
		String uri="/mypage/main";
		
		String memberId=(String)session.getAttribute("memberId");

			
			int hotelRevcnt =mps.searchHotelRevCnt(memberId);
			int dinningRevcnt =mps.searchDinningRevCnt(memberId);
			String name = mps.searchMemberName(memberId);
			List<EventDomain> eventList=mps.searchEventList();
			
			session.setAttribute("name", name);
			
			model.addAttribute("memberName", name);
			model.addAttribute("hotelRevCnt", hotelRevcnt);
			model.addAttribute("dinningRevcnt", dinningRevcnt);
			model.addAttribute("eventList", eventList);
			
		return uri;
	}//mypageView
	
	
}//class
