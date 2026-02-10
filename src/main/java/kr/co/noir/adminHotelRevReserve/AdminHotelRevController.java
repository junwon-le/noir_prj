package kr.co.noir.adminHotelRevReserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		arDTO.setUrl("/admin/hotelMember/");
		
		
		
		list=ahrs.SearchHotelRevList(arDTO);
		String pagiNation = ahrs.pagenation(arDTO);
		
		System.out.println(list);
		model.addAttribute("hotelMemberList", list);
		model.addAttribute("pagiNation", pagiNation);
		
		return "/manager/memberReserve/hotelRevList";
	}//hotelMemberDetail
	
	
	@GetMapping("/Hoteldetail")
	public String adminHotelMemberDetail () {		
		
		return "/manager/memberReserve/hotelRevDetail";
	}//hotelMemberDetail
	
	
	
}//class
