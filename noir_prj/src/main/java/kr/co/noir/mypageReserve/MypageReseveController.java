package kr.co.noir.mypageReserve;

import java.util.Enumeration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/mypage/reserve")
@Controller
public class MypageReseveController {
	
	@Autowired
	MypageReserveService mrs;
	
	@Autowired
	ReserveEmailService emailService;
	
	
	
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
		
		return "mypage/memberHotelRevList";
		
		
	}//HotelReserveList

	@ResponseBody
	@GetMapping("/hotelSearch")
	public String searchRevHotel(ReserveSearchDTO rsDTO, Model model,HttpSession session) {
		
		rsDTO.setMemberId((String)session.getAttribute("memberId"));


		rsDTO.setReserveType("room");
		return mrs.searchHotelRevList(rsDTO);
	}//searchRevHotel
	
	
	/* @PostMapping("/hotelRevDetail") */
	@RequestMapping(value = "/hotelRevDetail", method = {RequestMethod.GET, RequestMethod.POST})
	  public String hotelRevDetail(ReserveDetailDTO rdDTO,HttpSession session, Model model) { 
		  rdDTO.setMemberId(((String)session.getAttribute("memberId")));
		  

	  model.addAttribute("hotelRevDetail",  mrs.searchOneHotelRevDetail(rdDTO));
	  
	  
	  return "mypage/memberHotelRevDetail";
	  
	  }
	 
	  @PostMapping("/cancelHotelReserve")
	  public String cancelHotelReserve(HttpSession session,int reserveNum,Model model) {
		  String uri="mypage/memberHotelRevDetail";
		  //System.out.println("예약번호--------"+reserveNum);
		  int cnt = mrs.removeHotelReserve(reserveNum);
		  if(cnt<2) {
			 model.addAttribute("cancelFlag",true); 
			  
			  
		  }else {
			  
			  model.addAttribute("msg","예약취소가 완료되었습니다.");
			  uri="mypage/successPage";
			  
		  }//end else
		  
		  return uri;
	  }//cancelHotelReserve
	  
	  @GetMapping("/hotelRevDetail2")
	  public String hotelRevDetail2() {
		  
		  return "mypage/memberHotelRevDetail";
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
	
		System.out.println(rsDTO.getEndDate());
		System.out.println(rsDTO.getStartDate());
		System.out.println(rsDTO.getMemberId());
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
	  
	 
	// ==============이메일 발송 추가===========================
	/*
	 * @PostMapping("/sendReserveEmail") public String
	 * sendReserveEmail(ReserveDetailDTO rdDTO, HttpSession session,
	 * RedirectAttributes redirectAttributes) {
	 * rdDTO.setMemberId((String)session.getAttribute("memberId"));
	 * 
	 * List<HotelRevDetailDomain> details = mrs.searchOneHotelRevDetail(rdDTO);
	 * 
	 * if (details != null && !details.isEmpty()) {
	 * emailService.sendLargeDesignMail(details);
	 * redirectAttributes.addFlashAttribute("msg", "예약 확인서가 메일로 발송되었습니다."); }
	 * 
	 * // 파라미터를 붙여서 상세페이지 GET 요청으로 보냄 redirectAttributes.addAttribute("reserveNum",
	 * rdDTO.getReserveNum()); redirectAttributes.addAttribute("reserveType",
	 * rdDTO.getReserveType());
	 * 
	 * return "redirect:/mypage/reserve/hotelRevDetail"; }
	 */
	  
	  
	// ==============이메일 발송 추가 (호텔/다이닝 통합) ===========================
	  @PostMapping("/sendReserveEmail")
	  public String sendReserveEmail(ReserveDetailDTO rdDTO, HttpSession session, RedirectAttributes redirectAttributes) {
	      String memberId = (String) session.getAttribute("memberId");
	      rdDTO.setMemberId(memberId);
	      String type = rdDTO.getReserveType(); // "room" 또는 "dinning"
	      System.out.println("dsffdsdf"+type);
	      
	      String redirectUri = "";
	     
	      if ("room".equals(type)) {
	          // 1. 호텔 도메인 조회 및 발송
	          List<HotelRevDetailDomain> hotelDetails = mrs.searchOneHotelRevDetail(rdDTO);
	          if (hotelDetails != null && !hotelDetails.isEmpty()) {
	              emailService.sendHotelReserveMail(hotelDetails); // 호텔 전용 메서드
	              redirectAttributes.addFlashAttribute("msg", "객실 예약 확인서가 메일로 발송되었습니다.");
	              redirectAttributes.addFlashAttribute("emailFlag",true);
	          }
	          redirectUri = "redirect:/mypage/reserve/hotelRevDetail";

	      } else if ("dinning".equals(type)) {
	          // 2. 다이닝 도메인 조회 및 발송
	          // (다이닝 전용 조회 메서드와 도메인 객체 사용)
	    	  DinningRevDetailDomain diningDetail = mrs.searchOneDinningRevDetail(rdDTO);
	          if (diningDetail != null) {
	              emailService.sendDiningReserveMail(diningDetail); // 다이닝 전용 메서드
	              redirectAttributes.addFlashAttribute("msg", "다이닝 예약 확인서가 메일로 발송되었습니다.");
	              redirectAttributes.addFlashAttribute("emailFlag",true);
	          }
	          redirectUri = "redirect:/mypage/reserve/memberDinningDetail";
	      }

	      // 공통 파라미터 세팅
	      redirectAttributes.addAttribute("reserveNum", rdDTO.getReserveNum());
	      redirectAttributes.addAttribute("reserveType", type);
	      
	      return redirectUri; 
	  }
	  
	  
	  
	  
	  
	  
	  
	
}//class
