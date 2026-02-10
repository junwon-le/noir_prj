package kr.co.noir.adminDinningReserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
		 
		return "/manager/memberReserve/dinningRevList";
		
	}//AdminDinningRevView
	
	@GetMapping("/dinningDetail")
	public String dinningMemberDetail (int reserveNum,Model model) {
	
		model.addAttribute("adminMemberDininngDetail", adrs.serachOneDinningDetail(reserveNum));
		return "/manager/memberReserve/dinningRevDetail";
	}//dinningMemberDetail
	
	
	@GetMapping("/cancel")
	public String dinningReseveCancel(int reserveNum,Model model,RedirectAttributes rattr) {
		
		boolean flag= adrs.modifyDinningRev(reserveNum);
		
		System.out.println("결과는 "+flag);
		String msg="예약 취소를 실패하였습니다.";
		if(flag) {
			msg="예약취소가 완료되었습니다.";
		}//end if
		rattr.addFlashAttribute("flag", flag);
		model.addAttribute("msg",msg);
		return "redirect:/admin/dinningMember/";
		
	}//dinningReseveCancel
	
	
	
	
	
	
}
