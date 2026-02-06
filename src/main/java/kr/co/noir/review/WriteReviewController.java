package kr.co.noir.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class WriteReviewController {

    @Autowired
    private WriteReviewService reviewService;

    @GetMapping("/writeReview")
    public String reviewWriteForm(Model model, HttpSession session) {
    	String sessionMemberId = (String) session.getAttribute("memberId");

	    if (sessionMemberId == null) {
	        return "redirect:/login/memberLogin";
	    }

	    int memberNum = reviewService.getMemberNumById(sessionMemberId);
	    
        List<WriteReviewDTO> list = reviewService.getUnreviewedRooms(memberNum);
        model.addAttribute("unreviewedList", list);
        
        return "review/writeReview"; 
    }

    @PostMapping("/writeReview")
    public String reviewWrite(WriteReviewDTO dto, HttpSession session) {
    	String sessionMemberId = (String) session.getAttribute("memberId");

	    if (sessionMemberId == null) {
	        return "redirect:/login/memberLogin";
	    }

	    int memberNum = reviewService.getMemberNumById(sessionMemberId);
        
	    dto.setMemberNum(memberNum);
	    
        try {
            reviewService.registerReview(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/error"; 
        }
        
        return "redirect:/review/myReviewList";
    }
}
