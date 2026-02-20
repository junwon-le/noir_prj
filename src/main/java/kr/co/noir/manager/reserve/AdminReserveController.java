package kr.co.noir.manager.reserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RequestMapping("/admin")
@Controller
public class AdminReserveController {

	@Autowired
	private AdminReserveService ars;
	
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
		return "manager/reserve/nonRoomRes";
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
		
		return "manager/reserve/nonDinningRes";
	}//nonDinningReserve
	@GetMapping("/nonRoomResDetail")
	public String nonRoomResDetail(int resNum, Model model) {
		List<NonRoomDetailDomain> roomDetail = ars.searchNonRoomDetail(resNum);
		
		model.addAttribute("room",roomDetail);
		return "manager/reserve/nonRoomResDetail";
	}//nonRoomResDetail
	
	@GetMapping("/nonDinningResDetail")
	public String nonDinningResDetail(int resNum, Model model) {
		NonDinningDetailDomain dinningDetail = ars.searchnonDinningDetail(resNum);
		
		model.addAttribute("dinning",dinningDetail);
		return "manager/reserve/nonDinningResDetail";
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
}
