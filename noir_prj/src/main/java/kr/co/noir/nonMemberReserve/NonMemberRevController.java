package kr.co.noir.nonMemberReserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import kr.co.noir.mypageReserve.DinningRevDetailDomain;
import kr.co.noir.mypageReserve.HotelRevDetailDomain;
import kr.co.noir.mypageReserve.ReserveDetailDTO;
import kr.co.noir.mypageReserve.ReserveEmailService;

@RequestMapping("/nonMember")
@Controller
public class NonMemberRevController {
	
	@Autowired
	private NonMemberRevService nmrs;
	
	@Autowired
	private ReserveEmailService emailService;
	
	
	@PostMapping("/reserveCheck")
	public String reserveCheck(NonMemberRevDTO nmrDTO,Model model,RedirectAttributes rattr) {
	
		boolean reserveFlag = true;
		String reserveType=nmrDTO.getReserveType();
		String uri = "redirect:/login/memberLogin";
		
		System.out.println(nmrDTO.getReserveType());
		System.out.println("이메일"+nmrDTO.getEmail());
		System.out.println("비밀번호"+nmrDTO.getPassword());
		System.out.println("예약번호"+nmrDTO.getReserveNum());
		
		reserveFlag=nmrs.NonReserveCheck(nmrDTO);
//		System.out.println("확인결과"+reserveType);
//		System.out.println("확인결과"+reserveFlag);
		if(reserveFlag) {//받은 예약들이 존재하고 비밀번호가 맞는 경우 
			if("room".equals(reserveType)) {//reserveType이 room인 경우
				
				//roomDetailServic 넣기
				//model로해서 값보내기
				
				model.addAttribute("hotelRevDetail",  nmrs.searchOneHotelRevDetail(nmrDTO));
				uri="nonReserve/nonMemberHotelRevDetail";
			}else {
				
				//dinningDetailServic 넣기
				//model로해서 값보내기
				model.addAttribute("dinningRevDetail",  nmrs.searchOneDinningRevDetail(nmrDTO));
				
				uri="nonReserve/nonMemberDinningDetail";
			}//end else
				
		}else {
				rattr.addFlashAttribute("reserveFlag",!reserveFlag );
			
		}//end if		
	
		
		
		
		return 	uri;
		
	}//reserveCheck
	
	
	@PostMapping("/sendReserveEmail")
	  public String sendNonReserveEmail(NonMemberRevDTO nmrDTO, Model model) {
	      String type = nmrDTO.getReserveType();
	      System.out.println("dsffdsdf"+type);
	      
	      String uri = "";
	     
	      if ("room".equals(type)) {
	          // 1. 호텔 도메인 조회 및 발송
	          List<HotelRevDetailDomain> hotelDetails = nmrs.searchOneHotelRevDetail(nmrDTO);
	          if (hotelDetails != null && !hotelDetails.isEmpty()) {
	              emailService.sendHotelReserveMail(hotelDetails); // 호텔 전용 메서드
	              model.addAttribute("msg", "객실 예약 확인서가 메일로 발송되었습니다.");
	              model.addAttribute("emailFlag", true);
	             }
	          model.addAttribute("hotelRevDetail",  nmrs.searchOneHotelRevDetail(nmrDTO));
	          uri = "nonReserve/nonMemberHotelRevDetail";

	      } else if ("dinning".equals(type)) {
	          // 2. 다이닝 도메인 조회 및 발송
	          // (다이닝 전용 조회 메서드와 도메인 객체 사용)
	    	  DinningRevDetailDomain diningDetail = nmrs.searchOneDinningRevDetail(nmrDTO);
	          if (diningDetail != null) {
	              emailService.sendDiningReserveMail(diningDetail); // 다이닝 전용 메서드
	              model.addAttribute("msg", "다이닝 예약 확인서가 메일로 발송되었습니다.");
	              model.addAttribute("emailFlag", true); 
	          }
	          model.addAttribute("dinningRevDetail",  nmrs.searchOneDinningRevDetail(nmrDTO));
	          uri = "nonReserve/nonMemberDinningDetail";
	      }
	      // 공통 파라미터 세팅
	      model.addAttribute("reserveNum", nmrDTO.getReserveNum());
	      model.addAttribute("reserveType", type);
	      
	      return uri; 
	}
}//class
