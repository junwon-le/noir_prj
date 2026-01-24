package kr.co.noir.mypageReserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;


@RequestMapping("/mypage/reserve")
@Controller
public class MypageReseveController {
	
	@Autowired
	MypageReserveService mrs;
	
//=========호텔 에약 리스트==================
	@GetMapping("/memberHotelList")
	public String hotelReserveList(HttpSession session) {
		
		return "/mypage/memberHotelRevList";
		
		
	}//HotelReserveList

	@ResponseBody
	@GetMapping("/hotelSearch")
	public String searchRevHotel(ReserveSearchDTO rsDTO, Model model) {
		
		rsDTO.setMemberId("user2");
		
		int totalCount=mrs.totalCnt(rsDTO);
		int pageScale=mrs.pageScale();
		int totalPage=mrs.totalPage(totalCount, pageScale);
		int currentPage=rsDTO.getCurrentPage();
		int startNum=mrs.startNum(currentPage, pageScale);
		int endNum=mrs.endNum(startNum, pageScale);
		
		rsDTO.setStartNum(startNum);
		rsDTO.setEndNum(endNum);
		rsDTO.setUrl("/mypage/memberHotelRevList");
		
		List<HotelRevSearchDomain> list = mrs.searchHotelRevList(rsDTO);
		
		model.addAttribute("hotelRevList", list);
		
		System.out.println(mrs.searchHotelRevList(rsDTO));
		System.out.println(mrs.totalCnt(rsDTO));
		return "/mypage/memberHotelRevList";
	}//searchRevHotel
	

	
	
//============다이닝 예약 리스트==================
	
	@GetMapping("/memberDinningList")
	public String dinningReserveList(HttpSession session) {
		
		return "/mypage/memberDinningRevList";
	}//dinningReserveList
	
	
}//class
