package kr.co.noir.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kr.co.noir.notice.BoardRangeDTO;

@Controller
public class EventController {
	
	@Autowired
	private EventService es;
	
	@GetMapping("/eventList")
    public String myReviewList(BoardRangeDTO rDTO, Model model, HttpSession session) {

		rDTO.setUrl("eventList");
		
        int totalCount = es.totalCnt(rDTO);
        int pageScale = es.pageScale();
        int totalPage = es.totalPage(totalCount, pageScale);
        int currentPage = rDTO.getCurrentPage();
        
        rDTO.setStartNum(es.startNum(currentPage, pageScale));
        rDTO.setEndNum(es.endNum(rDTO.getStartNum(), pageScale));
        rDTO.setTotalPage(totalPage);

        List<EventDomain> EventList = es.getEventList(rDTO);
        
        model.addAttribute("EventList", EventList);
        model.addAttribute("pagenation", es.pagination2(rDTO, "center"));
        
        return "event/eventList";
    }

  
	
	@GetMapping("/eventDetail")
    public String eventDetail(@RequestParam("eventNum") int eventNum, Model model) {
        EventDomain eventDetail = es.searchOneBoard(eventNum);
        
        model.addAttribute("eventDetail", eventDetail);
        
        return "event/eventDetail"; 
    }
	

}//class
