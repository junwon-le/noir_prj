package kr.co.noir.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/notice")
public class NoticeController {
	
	@Autowired //field 의존성 주입
	private NoticeService bs;
	
	@GetMapping("/noticeList")
	public String boardList(BoardRangeDTO rDTO, Model model) {
		
		int totalCount = bs.totalCnt(rDTO);//총 게시물의 수
		int pageScale = bs.pageScale(); //한페이지에 보여줄 게시물의 수
		int totalPage = bs.totalPage(totalCount,pageScale);//총 페이지 수
		int currentPage = rDTO.getCurrentPage(); //현재 페이지
		int startNum = bs.startNum(currentPage, pageScale); // 시작번호
		int endNum = bs.endNum(startNum, pageScale); // 끝 번호
		
		rDTO.setStartNum(startNum);
		rDTO.setEndNum(endNum);
		rDTO.setTotalPage(totalPage);
		rDTO.setUrl("/notice/noticeList");
		//rDTO.setCurrentPage(currentPage); 1이 기본이라서 안넣어도됨
		
		List<NoticeDomain> noticeList = bs.searchBoardList(rDTO);//게시물의 내용
		String pagination = bs.pagination2(rDTO,"center");//페이지 네이션
		
		
		model.addAttribute("listNum",totalCount-(currentPage-1)*pageScale);
		model.addAttribute("noticeList",noticeList);
		model.addAttribute("pagenation",pagination);
		
		return "notice/noticeList";
	}
	
	@GetMapping("/noticeDetail")
	@ResponseBody
	public NoticeDomain detail(@RequestParam int noticeNum) {
	    return bs.searchOneBoard(noticeNum); 
	}
	
	
}//class
