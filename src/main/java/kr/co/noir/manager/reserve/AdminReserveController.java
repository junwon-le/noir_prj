package kr.co.noir.manager.reserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.noir.mypageReserve.DinningRevDetailDomain;
import kr.co.noir.mypageReserve.HotelRevDetailDomain;
import kr.co.noir.mypageReserve.ReserveEmailService;
import kr.co.noir.nonMemberReserve.NonMemberRevDTO;
import kr.co.noir.nonMemberReserve.NonMemberRevService;


@RequestMapping("/admin")
@Controller
public class AdminReserveController {

	@Autowired
	private AdminReserveService ars;
	
	@Autowired
	private NonMemberRevService nmrs;
	
	@Autowired
	private ReserveEmailService emailService;
	
	
	@GetMapping("/nonRoomReserve")
	public String nonRoomReserve(AdminResRangeDTO arrDTO, Model model) {
		int totalCnt = ars.totalCnt(arrDTO);
		int pageScale = ars.pageScale();
		int currentPage = arrDTO.getCurrentPage();
		int startNum = ars.startNum(pageScale, currentPage);
		int endNum = ars.endNum(startNum, pageScale);
		arrDTO.setTotalPage(totalCnt/pageScale+1);
		arrDTO.setEndNum(endNum);
		arrDTO.setStartNum(startNum);
		arrDTO.setCurrentPage(currentPage);
		List<NonRoomResDomain> list = ars.searchNonRoomList(arrDTO);
		String pagination = ars.pagination(arrDTO, "center");
		
		model.addAttribute("list",list);
		model.addAttribute("pagination",pagination);
		return "/manager/reserve/nonRoomRes";
	}
	
	@GetMapping("/nonDinningReserve")
	public String nonDinningReserve(AdminResRangeDTO arrDTO, Model model) {
		int totalCnt = ars.DinningTotalCnt(arrDTO);
		int pageScale = ars.pageScale();
		int currentPage = arrDTO.getCurrentPage();
		int startNum = ars.startNum(pageScale, currentPage);
		int endNum = ars.endNum(startNum, pageScale);
		arrDTO.setTotalPage(totalCnt/pageScale+1);
		arrDTO.setEndNum(endNum);
		arrDTO.setStartNum(startNum);
		arrDTO.setCurrentPage(currentPage);
		List<NonRoomResDomain> list = ars.searchNonDinngingList(arrDTO);
		String pagination = ars.paginationDinning(arrDTO, "center");
		
		model.addAttribute("list",list);
		model.addAttribute("pagination",pagination);
		
		System.out.println(arrDTO);
		
		return "/manager/reserve/nonDinningRes";
	}//nonDinningReserve
	@GetMapping("/nonRoomResDetail")
	public String nonRoomResDetail(int resNum, Model model) {
		List<NonRoomDetailDomain> roomDetail = ars.searchNonRoomDetail(resNum);
		
		model.addAttribute("room",roomDetail);
		return "/manager/reserve/nonRoomResDetail";
	}//nonRoomResDetail
	
	@GetMapping("/nonDinningResDetail")
	public String nonDinningResDetail(int resNum, Model model) {
		NonDinningDetailDomain dinningDetail = ars.searchnonDinningDetail(resNum);
		
		model.addAttribute("dinning",dinningDetail);
		return "/manager/reserve/nonDinningResDetail";
	}//nonDinningResDetail
	
	@GetMapping("/nonDinningRefund")
	public String nonDinningRefund(int resNum, AdminResRangeDTO arrDTO , Model model, RedirectAttributes rab) {
		boolean flag = ars.modifyRes(resNum);
		System.out.println(arrDTO);
		rab.addAttribute("currentPage", arrDTO.getCurrentPage());
		rab.addAttribute("keyword", arrDTO.getKeyword());
		rab.addAttribute("field", arrDTO.getField());
		rab.addFlashAttribute("flag", flag);
		
		return "redirect:/admin/nonDinningReserve";
	}//nonDinningResDetail
	@GetMapping("/nonRoomRefund")
	public String nonRoomRefund(int resNum, AdminResRangeDTO arrDTO , Model model, RedirectAttributes rab) {
		boolean flag = ars.modifyRes(resNum);
		System.out.println(arrDTO);
		rab.addAttribute("currentPage", arrDTO.getCurrentPage());
		rab.addAttribute("keyword", arrDTO.getKeyword());
		rab.addAttribute("field", arrDTO.getField());
		rab.addFlashAttribute("flag", flag);
		
		return "redirect:/admin/nonRoomReserve";
	}//nonDinningResDetail
	
	
	@PostMapping("/sendNonReserveEmail")
	  public String sendNonReserveEmail(NonMemberRevDTO nmrDTO, Model model) {
	      String type = nmrDTO.getReserveType();
	      System.out.println("dsffdsdf"+type);
	      int num =Integer.parseInt( nmrDTO.getReserveNum());
	      
	      String uri = "";
	     
	      if ("room".equals(type)) {
	          // 1. 호텔 도메인 조회 및 발송
	          List<HotelRevDetailDomain> hotelDetails = nmrs.searchOneHotelRevDetail(nmrDTO);
	          if (hotelDetails != null && !hotelDetails.isEmpty()) {
	              emailService.sendHotelReserveMail(hotelDetails); // 호텔 전용 메서드
	              model.addAttribute("msg", "객실 예약 확인서가 메일로 발송되었습니다.");
	              model.addAttribute("emailFlag", true);
	             }
	  		List<NonRoomDetailDomain> roomDetail = ars.searchNonRoomDetail(num);
			
			model.addAttribute("room",roomDetail);
	       //   model.addAttribute("hotelRevDetail",  nmrs.searchOneHotelRevDetail(nmrDTO));
	          uri = "/manager/reserve/nonRoomResDetail";

	      } else if ("dinning".equals(type)) {
	          // 2. 다이닝 도메인 조회 및 발송
	          // (다이닝 전용 조회 메서드와 도메인 객체 사용)
	    	  DinningRevDetailDomain diningDetail = nmrs.searchOneDinningRevDetail(nmrDTO);
	          if (diningDetail != null) {
	              emailService.sendDiningReserveMail(diningDetail); // 다이닝 전용 메서드
	              model.addAttribute("msg", "다이닝 예약 확인서가 메일로 발송되었습니다.");
	              model.addAttribute("emailFlag", true); 
	          }
	          NonDinningDetailDomain dinningDetail = ars.searchnonDinningDetail(num);
		  		
		  		model.addAttribute("dinning",dinningDetail);
	          
	          //model.addAttribute("dinningRevDetail",  nmrs.searchOneDinningRevDetail(nmrDTO));
	          uri = "/manager/reserve/nonDinningResDetail";
	      }
	      // 공통 파라미터 세팅
	      model.addAttribute("reserveNum", nmrDTO.getReserveNum());
	      model.addAttribute("reserveType", type);
	      
	      return uri; 
	}
}
