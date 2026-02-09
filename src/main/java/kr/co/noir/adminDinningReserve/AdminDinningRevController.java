package kr.co.noir.adminDinningReserve;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/admin/dinningMember")
@Controller
public class AdminDinningRevController {

	
	@Autowired
	private AdminDinningRevService adrs;
	
	
	@GetMapping("/")
	public String AdminDinningRevView(Model model,AdminRangeDTO arDTO) {
		
		arDTO.setReserveType("dinning");
		
		int totalCount=adrs.totalCnt(arDTO);
		
		System.out.println(totalCount);
		int pageScale=adrs.pageScale();
		int totalPage=adrs.totalPage(totalCount, pageScale);
		int currentPage =arDTO.getCurrentPage();
		int startNum=adrs.startNum(currentPage, pageScale);
		int endNum=adrs.endNum(startNum, pageScale);
		
		arDTO.setStartNum(startNum);
		arDTO.setEndNum(endNum);
		arDTO.setTotalPage(totalPage);
		arDTO.setUrl("/admin/dinningMember/");
		
		
		
		List<AdminDinningRevDomain> list =adrs.SearchDinningRevList(arDTO);
		String pagiNation=adrs.pagenation(arDTO);
		
		model.addAttribute("DinningRevList", list);
		model.addAttribute("pagiNation", pagiNation);
		 
		return "/manager/dinningReserve/dinningRevList";
		
	}//AdminDinningRevView
	
	@GetMapping("/dinningDetail")
	public String dinningMemberDetail (int reserveNum,Model model) {
	
		model.addAttribute("memberDininngDetail", adrs.serachOneDinningDetail(reserveNum));
		return "/manager/dinningReserve/dinningRevDetail";
	}//dinningMemberDetail
	
	@GetMapping("/Hoteldetail")
	public String hotelMemberDetail () {
		
		
		return "/manager/hotelReserve/hotelRev_detail";
	}
	
	@GetMapping("/HotelRev")
	public String hotelMeDetail () {
		
		
		return "/manager/hotelReserve/hotelRev";
	}
	
	
}
