package kr.co.noir.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;
import kr.co.noir.notice.BoardRangeDTO;

@Controller
public class ReviewController {
	
	@Autowired //field 의존성 주입
	private ReviewService rs;
	
	@GetMapping("/myReviewList")
    public String myReviewList(BoardRangeDTO rDTO, Model model, HttpSession session) {
		String sessionMemberId = (String) session.getAttribute("memberId");

	    if (sessionMemberId == null) {
	        return "redirect:/login/memberLogin";
	    }

	    int memberNum = rs.getMemberNumById(sessionMemberId);
	    
	    rDTO.setMemberNum(memberNum);
	    rDTO.setUrl("/myReviewList");

        int totalCount = rs.totalCnt(rDTO);
        int pageScale = rs.pageScale();
        int totalPage = rs.totalPage(totalCount, pageScale);
        int currentPage = rDTO.getCurrentPage();
        
        rDTO.setStartNum(rs.startNum(currentPage, pageScale));
        rDTO.setEndNum(rs.endNum(rDTO.getStartNum(), pageScale));
        rDTO.setTotalPage(totalPage);

        List<ReviewDomain> myReviewList = rs.getReviewList(rDTO);
        
        model.addAttribute("myReviewList", myReviewList);
        model.addAttribute("pagenation", rs.pagination2(rDTO, "center"));
        
        return "review/myReviewList";
    }

    @GetMapping("/roomReviewList")
    public String roomReviewList(@RequestParam(value="num", defaultValue="1") int num, 
                                 BoardRangeDTO rDTO, Model model) {
        rDTO.setRoomTypeNum(num);
        rDTO.setUrl("/roomReviewList");

        int totalCount = rs.roomTotalCnt(rDTO);
        int pageScale = rs.pageScale();
        int totalPage = rs.totalPage(totalCount, pageScale);
        int currentPage = rDTO.getCurrentPage();

        rDTO.setStartNum(rs.startNum(currentPage, pageScale));
        rDTO.setEndNum(rs.endNum(rDTO.getStartNum(), pageScale));
        rDTO.setTotalPage(totalPage);

        List<ReviewDomain> roomReviewList = rs.getReviewRoomList(rDTO);

        model.addAttribute("roomReviewList", roomReviewList);
        model.addAttribute("pagenation", rs.pagination2(rDTO, "center"));
        
        return "review/roomReviewList";
    }
	
	@GetMapping("/reviewDetail")
	@ResponseBody
	public ReviewDomain detail(@RequestParam int reviewNum) {
	    return rs.searchOneBoard(reviewNum); 
	}
	

}//class
