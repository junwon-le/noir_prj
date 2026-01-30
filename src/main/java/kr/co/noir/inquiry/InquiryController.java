package kr.co.noir.inquiry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.noir.notice.BoardRangeDTO;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {
	
	@Autowired //field 의존성 주입
	private InquiryService bs;
	
	@GetMapping("/inquiryList")
	public String boardList(BoardRangeDTO rDTO, Model model, HttpSession session) {
		
//	    Integer sessionMemberNum = (Integer) session.getAttribute("memberNum");
//	    
//	    if (sessionMemberNum == null) {
//	        return "redirect:/login/loginPage";
//	    }
		int sessionMemberNum = 97;
	    
	    rDTO.setMemberNum(sessionMemberNum);
		
		int totalCount = bs.totalCnt(rDTO);//총 게시물의 수
		int pageScale = bs.pageScale(); //한페이지에 보여줄 게시물의 수
		int totalPage = bs.totalPage(totalCount,pageScale);//총 페이지 수
		int currentPage = rDTO.getCurrentPage(); //현재 페이지
		int startNum = bs.startNum(currentPage, pageScale); // 시작번호
		int endNum = bs.endNum(startNum, pageScale); // 끝 번호
		
		rDTO.setStartNum(startNum);
		rDTO.setEndNum(endNum);
		rDTO.setTotalPage(totalPage);
		rDTO.setUrl("/inquiry/inquiryList");
		//rDTO.setCurrentPage(currentPage); 1이 기본이라서 안넣어도됨
		
		List<InquiryDomain> inquiryList = bs.searchBoardList(rDTO);//게시물의 내용
		String pagination = bs.pagination2(rDTO,"center");//페이지 네이션
		
		
		model.addAttribute("listNum",totalCount-(currentPage-1)*pageScale);
		model.addAttribute("inquiryList",inquiryList);
		model.addAttribute("pagenation",pagination);
		
		return "inquiry/inquiryList";
	}
	
	@GetMapping("/inquiryDetail")
	@ResponseBody
	public InquiryDomain detail(@RequestParam int inquiryNum) {
	    return bs.searchOneBoard(inquiryNum); 
	}
	
	@GetMapping("/writeInquiry")
	public String inquiryWriteForm(Model model) {
	    
	    int testMemberNum = 97;
	    InquiryDomain memberInfo = bs.getMemberInfo(testMemberNum);
	    
	    if(memberInfo == null) memberInfo = new InquiryDomain();
	    
	    model.addAttribute("member", memberInfo);
	    
	    // 파일 이름이 writeInquiry.html 이니까 아래처럼 리턴!
	    return "inquiry/writeInquiry"; 
	}

	@PostMapping("/writeInquiry") 
	public String inquiryWriteAction(InquiryDTO iDTO) {
	    iDTO.setMemberNum(97); 
	    bs.addInquiry(iDTO);
	    return "redirect:/inquiry/inquiryList";
	}
	
}//class
