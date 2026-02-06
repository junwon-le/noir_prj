package kr.co.noir.inquiry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.noir.notice.BoardRangeDTO;

@Controller
public class InquiryController {
	
	@Autowired //field 의존성 주입
	private InquiryService is;
	
	@GetMapping("/inquiryList")
	public String boardList(BoardRangeDTO rDTO, Model model, HttpSession session) {
		
	    String sessionMemberId = (String) session.getAttribute("memberId");

	    if (sessionMemberId == null) {
	        return "redirect:/login/memberLogin";
	    }

	    int memberNum = is.getMemberNumById(sessionMemberId);
	    
	    rDTO.setMemberNum(memberNum);
		
		int totalCount = is.totalCnt(rDTO);//총 게시물의 수
		int pageScale = is.pageScale(); //한페이지에 보여줄 게시물의 수
		int totalPage = is.totalPage(totalCount,pageScale);//총 페이지 수
		int currentPage = rDTO.getCurrentPage(); //현재 페이지
		int startNum = is.startNum(currentPage, pageScale); // 시작번호
		int endNum = is.endNum(startNum, pageScale); // 끝 번호
		
		rDTO.setStartNum(startNum);
		rDTO.setEndNum(endNum);
		rDTO.setTotalPage(totalPage);
		rDTO.setUrl("/inquiryList");
		//rDTO.setCurrentPage(currentPage); 1이 기본이라서 안넣어도됨
		
		List<InquiryDomain> inquiryList = is.searchBoardList(rDTO);//게시물의 내용
		String pagination = is.pagination2(rDTO,"center");//페이지 네이션
		
		
		model.addAttribute("listNum",totalCount-(currentPage-1)*pageScale);
		model.addAttribute("inquiryList",inquiryList);
		model.addAttribute("pagenation",pagination);
		
		return "inquiry/inquiryList";
	}
	
	@GetMapping("/inquiryDetail")
	@ResponseBody
	public InquiryDomain detail(@RequestParam int inquiryNum) {
	    return is.searchOneBoard(inquiryNum); 
	}
	
	@GetMapping("/writeInquiry")
	public String inquiryWriteForm(Model model, HttpSession session) {
	    
		String sessionMemberId = (String) session.getAttribute("memberId");

	    if (sessionMemberId == null) {
	        return "redirect:/login/memberLogin";
	    }

	    int memberNum = is.getMemberNumById(sessionMemberId);
	    InquiryDomain memberInfo = is.getMemberInfo(memberNum);
	    
	    if(memberInfo == null) memberInfo = new InquiryDomain();
	    
	    model.addAttribute("member", memberInfo);
	    
	    return "inquiry/writeInquiry"; 
	}

	@PostMapping("/writeInquiry") 
	public String inquiryWriteAction(InquiryDTO iDTO,HttpSession session) {
		String sessionMemberId = (String) session.getAttribute("memberId");

	    if (sessionMemberId == null) {
	        return "redirect:/login/memberLogin";
	    }

	    int memberNum = is.getMemberNumById(sessionMemberId);
	    
	    iDTO.setMemberNum(memberNum);
	    is.addInquiry(iDTO);
	    return "redirect:/inquiryList";
	}
	
}//class
