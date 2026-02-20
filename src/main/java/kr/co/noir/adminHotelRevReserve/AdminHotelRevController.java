package kr.co.noir.adminHotelRevReserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.noir.adminDinningReserve.AdminRangeDTO;

@RequestMapping ("/admin/hotelMember")
@Controller
public class AdminHotelRevController {
	
	
	@Autowired
	private AdminHotelRevService ahrs;
	
	@GetMapping("/")
	public String adminHotelMemberList (Model model,AdminRangeDTO arDTO) {
		
		
		List<AdminHotelRevSearchDomain> list =null;
		
		
	 int totalCount=ahrs.totalCnt(arDTO);
		
		System.out.println(totalCount);
		int pageScale=ahrs.pageScale();
		int totalPage=ahrs.totalPage(totalCount, pageScale);
		int currentPage =arDTO.getCurrentPage();
		int startNum=ahrs.startNum(currentPage, pageScale);
		int endNum=ahrs.endNum(startNum, pageScale);
		
		
		arDTO.setStartNum(startNum);
		arDTO.setEndNum(endNum);
		arDTO.setTotalPage(totalPage);
		arDTO.setUrl("admin/hotelMember/");
		
		
		
		list=ahrs.SearchHotelRevList(arDTO);
		String pagiNation = ahrs.pagenation(arDTO);
		
		System.out.println(list);
		model.addAttribute("hotelMemberList", list);
		model.addAttribute("pagiNation", pagiNation);
		
		return "manager/memberReserve/hotelRevList";
	}//hotelMemberDetail
	
	
	@GetMapping("/hotelDetail")
	public String adminHotelMemberDetail(int reserveNum, Model model) {		
		
		List<AdminHotelRevDetailDomain> list =null;
		
		list=ahrs.searchOneHotelDetail(reserveNum);
		model.addAttribute("hotelMemberDetail", list);

		System.out.println("결과는요~~"+list);
		return "manager/memberReserve/hotelRevDetail";
	}//hotelMemberDetail
	
	@GetMapping("/cancel")
	public String hotelReseveCancel(int reserveNum,Model model,RedirectAttributes rattr) {
		
		boolean flag= ahrs.modifyHotelRev(reserveNum);
		
		System.out.println("결과는 "+flag);
		String msg="예약 취소를 실패하였습니다.";
		if(flag) {
			msg="예약취소가 완료되었습니다.";
		}//end if
		rattr.addFlashAttribute("flag", flag);
		rattr.addFlashAttribute("msg",msg);
		return "redirect:/admin/hotelMember/";
		
	}//dinningReseveCancel
	
	
	
}//class
