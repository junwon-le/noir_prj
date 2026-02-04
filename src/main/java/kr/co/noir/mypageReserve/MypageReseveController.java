package kr.co.noir.mypageReserve;

import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/mypage/reserve")
@Controller
public class MypageReseveController {
	
	@Autowired
	MypageReserveService mrs;
	
	
	@GetMapping("/session/check")
	@ResponseBody
	public String checkSession(HttpSession session) {

	    Enumeration<String> names = session.getAttributeNames();

	    StringBuilder sb = new StringBuilder();
	    while (names.hasMoreElements()) {
	        String name = names.nextElement();
	        Object value = session.getAttribute(name);

	        sb.append(name)
	          .append(" = ")
	          .append(value)
	          .append("\n");
	    }
	    return sb.toString();
	}
	
	
	
//=========호텔 에약 리스트==================
	@GetMapping("/memberHotelList")
	public String hotelReserveList(HttpSession session) {
		
		return "/mypage/memberHotelRevList";
		
		
	}//HotelReserveList

	@ResponseBody
	@GetMapping("/hotelSearch")
	public String searchRevHotel(ReserveSearchDTO rsDTO, Model model,HttpSession session) {
		
		rsDTO.setMemberId((String)session.getAttribute("memberId"));

//		System.out.println(rsDTO.getMemberId());
//		System.out.println(mrs.searchHotelRevList(rsDTO));
		rsDTO.setReserveType("room");
		return mrs.searchHotelRevList(rsDTO);
	}//searchRevHotel
	
	
	
	  @PostMapping("/hotelRevDetail") 
	  public String hotelRevDetail(ReserveDetailDTO rdDTO,HttpSession session, Model model) { 
		  rdDTO.setMemberId(((String)session.getAttribute("memberId")));
		  
	  System.out.println(rdDTO.getReserveNum());
	  System.out.println(rdDTO.getMemberId());
	  System.out.println(rdDTO.getReserveType());
	  
	  System.out.println("출력된 리스트---------"+mrs.searchOneHotelRevDetail(rdDTO));
	  model.addAttribute("hotelRevDetail",  mrs.searchOneHotelRevDetail(rdDTO));
	  
	  
	  return "/mypage/memberHotelRevDetail";
	  
	  }
	 
	  @PostMapping("/cancelHotelReserve")
	  public String cancelHotelReserve(HttpSession session,int reserveNum,Model model) {
		  String uri="/mypage/memberHotelRevDetail";
		  System.out.println("예약번호--------"+reserveNum);
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
		  
		  return "/mypage/memberHotelRevDetail";
	  }
		  
		 
	  
	
	

	
	
//============다이닝 예약 리스트==================
	
	@GetMapping("/memberDinningList")
	public String dinningReserveList(HttpSession session) {
		
		return "/mypage/memberDinningRevList";
	}//dinningReserveList
	
	
	

	
	@ResponseBody
	@GetMapping("/dinningSearch")
	public String searchRevDinning(ReserveSearchDTO rsDTO,HttpSession sesison,Model model) {
		rsDTO.setMemberId((String)sesison.getAttribute("memberId"));
		
		rsDTO.setReserveType("dinning");
		
		return mrs.searchDinningRevList(rsDTO);
		
		
	}//searchRevDinning
	
	@GetMapping("/memberDinningDetail")
	public String memberDinningDetail(HttpSession session,ReserveDetailDTO rdDTO,Model model) {
		rdDTO.setMemberId((String)session.getAttribute("memberId"));
		
		System.out.println(mrs.searchOneDinningRevDetail(rdDTO));
		model.addAttribute("dinningRevDetail",  mrs.searchOneDinningRevDetail(rdDTO));
		return "/mypage/memberDinningDetail";
	}//memberDinningDetail
	
	
	  @PostMapping("/cancelDinningReserve")
	  public String cancelDinningReserve(HttpSession session,int reserveNum,Model model) {
		  String uri="/mypage/memberHotelRevDetail";
		  System.out.println("예약번호--------"+reserveNum);
		  boolean flag = mrs.removeDinningReserve(reserveNum);
		  if(flag!=true) {
			 model.addAttribute("cancelFlag",true); 
			  
			  
		  }else {
			  
			  model.addAttribute("msg","예약취소가 완료되었습니다.");
			  uri="/mypage/successPage";
			  
		  }//end else
		  
		  return uri;
	  }//cancelDinningReserve
	
	
	
	
}//class
