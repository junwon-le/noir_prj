package kr.co.noir.adminDinningReserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.noir.adminHotelRevReserve.AdminReserveEmailService;
import kr.co.noir.mypageReserve.DinningRevDetailDomain;


@RequestMapping("/admin/dinningMember")
@Controller
public class AdminDinningRevController {

	
	@Autowired
	private AdminDinningRevService adrs;
	
	@Autowired
	private AdminReserveEmailService ares;
	

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
		System.out.println("릿그트의 결과는"+list);
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
		rattr.addFlashAttribute("msg",msg);
		return "redirect:/admin/dinningMember/";
		
	}//dinningReseveCancel
	
	
	 @PostMapping("/sendReserveEmail")
	  public String sendReserveEmail( @RequestParam(defaultValue = "0") int reserveNum,Model model, RedirectAttributes redirectAttributes) {
	      String redirectUri = "";
	     
	      DinningRevDetailDomain list = adrs.serachOneDinningDetail(reserveNum);
	      if (list != null) {
	        	  ares.sendDiningReserveMail(list);
	        	  redirectAttributes.addFlashAttribute("msg", "다이닝 예약 확인서가 발송되었습니다.");
	              redirectAttributes.addFlashAttribute("emailFlag", true);
	          }
	           
	          model.addAttribute("adminMemberDininngDetail", list);
	          
	          redirectAttributes.addAttribute("reserveNum", reserveNum);
	          redirectUri = "redirect:/admin/dinningMember/dinningDetail";

 
	      // 공통 파라미터 세팅
	      
	      return redirectUri; 
	  }
	
	
	
	
	
	
}
