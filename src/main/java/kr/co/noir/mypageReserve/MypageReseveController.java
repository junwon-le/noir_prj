package kr.co.noir.mypageReserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import jakarta.servlet.http.HttpSession;
import retrofit2.http.GET;


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
	public String searchRevHotel(ReserveSearchDTO rsDTO, Model model,HttpSession session) {
		
		rsDTO.setMemberId((String)session.getAttribute("memberId"));

		System.out.println(rsDTO.getMemberId());
		System.out.println(mrs.searchHotelRevList(rsDTO));
		
		return mrs.searchHotelRevList(rsDTO);
	}//searchRevHotel
	
	
	
	  @PostMapping("/hotelRevDetail") 
	  public String hotelRevDetail(ReserveDetailDTO rdDTO,HttpSession session, Model model) { 
//		  rdDTO.setMemberId(((String)session.getAttribute("memberId")));
		rdDTO.setMemberId("user40");
	  System.out.println(rdDTO.getReserveNum());
	  System.out.println(rdDTO.getMemberId());
	  System.out.println(rdDTO.getReserveType());
	  
	  System.out.println(mrs.searchOneHotelRevDetail(rdDTO));
	  model.addAttribute("hotelRevDetail",  mrs.searchOneHotelRevDetail(rdDTO));
	  
	  
	  return "/mypage/memberHotelRevDetail";
	  
	  }
	 
	  @PostMapping("/cancelHotelReserve")
	  public String cancelHotelReserve(HttpSession session,int reserveNum,Model model) {
		  String uri="/mypage/memberHotelRevDetail";
		  System.out.println(reserveNum);
		  int cnt = mrs.removeHotelReserve(reserveNum);
		  if(cnt<2) {
			 model.addAttribute("cancelFlag",true); 
			  
			  
		  }else {
			  
			  model.addAttribute("msg","예약취소가 완료되었습니다.");
			  uri="/mypage/successPage";
			  
		  }//end else
		  
		  return uri;
	  }//cancelHotelReserve
	  
	  @GetMapping("/hotelRevDetail2")
	  public String hotelRevDetail2() {
		  
		  return "/mypage/memberHotelRevDetail2";
	  }
		  
		 
	  
	
	

	
	
//============다이닝 예약 리스트==================
	
	@GetMapping("/memberDinningList")
	public String dinningReserveList(HttpSession session) {
		
		return "/mypage/memberDinningRevList";
	}//dinningReserveList
	
	
	@GetMapping("/memberDinningDetail")
	public String memberDinningDetail(HttpSession session) {
		
		return "/mypage/memberHotelDinningDetail";
	}//memberDinningDetail
	
	
	
	
	
}//class
