package kr.co.noir.event;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;


@RequestMapping("/event")
@Controller
public class EventAdminController {
		
		@Autowired
		private EventAdminService eas;
		
		//세션 체크
		private Integer getAdminNum(HttpSession session) {
		    return (Integer) session.getAttribute("adminNum");
		}

		
		/**
		 * =========================
		 * 1) 게시판 목록 조회 (List + Paging + Search)
		 * URL: GET /board/boardList
		 * 역할:
		 *  - 전체 게시글 수 조회
		 *  - 페이징 계산 (startNum, endNum, totalPage)
		 *  - 목록 조회 (RangeDTO로 범위/검색 전달)
		 *  - 페이지네이션 HTML 문자열 생성
		 *  - 화면(JSP)에 출력할 데이터 Model에 담기
		 * =========================
		 */
//		// 이벤트 목록 조회
		@GetMapping("/eventAdminList")
		public String eventAdminList(EventRangeDTO erDTO, Model model){
			int totalCnt = eas.totalCnt(erDTO); //총 게시물의 수
			int pageScale = eas.pageScale(); //한 화면에 보여줄 게시물의 수 
			int totalPage = eas.totalPage(totalCnt, pageScale); //총 페이지 수 
			int currentPage = erDTO.getCurrentPage();// 현재 페이지
			int startNum = eas.startNum(currentPage, pageScale); //시작 번호
			int endNum = eas.endNum(startNum, pageScale);// 끝 번호
			
			erDTO.setCurrentPage(currentPage);
			erDTO.setStartNum(startNum);
			erDTO.setEndNum(endNum);
			erDTO.setTotalPage(totalPage);
			erDTO.setUrl("/event/eventAdminList");
			
			List<EventAdminDomain> list = eas.searchEventList(erDTO);
			String pagination = eas.pagination(erDTO);
			//시작번호를 끝 번호 부터 처리
			int listNum = totalCnt-( currentPage-1)*pageScale;
			
			
			model.addAttribute("listNum", listNum);
			model.addAttribute("eventList", list);
			model.addAttribute("pagination", pagination);
			model.addAttribute("range", erDTO ); // 템플릿에서 range.keyword/currentPage 쓰려면 꼭 필요
			
			return "/manager/event/eventAdminList";
		}//boardList
		
		
//		// 이벤트 상세 조회
		@GetMapping("/eventAdminDetail")
	    public String eventAdminDetail(@RequestParam(defaultValue = "0") int eventNum ,Model model) {
			
	    	EventAdminDomain eaDomain = eas.searchOneEvent(eventNum);
	    		
	    		model.addAttribute("eaDomain",eaDomain);
	    		
	    		return "/manager/event/eventTotalProcess";
	    	}

//		// 이벤트 등록 페이지
		@GetMapping("/addAdminEventFrm")
		public String addAdminEventForm(HttpSession session, Model model) {

		    Integer adminNum = (Integer) session.getAttribute("adminNum");
		    if (adminNum == null) {
		        return "redirect:/adminLogin";
		    }

	    // 화면에서 관리자 번호를 쓸 일이 있으면
	    model.addAttribute("adminNum", adminNum);

	    return "/manager/event/eventTotalProcess";
		}//writeForm

//		// 이벤트 등록 프로세스
//		사진 두개는 멀티파트로 들어가고 다른 내용은 dto로
//		멀티파트에서 받은 파일명을 String으로 변환해서 dto로 넣어야 함
		@PostMapping("/addAdminEventProcess")
		public String addAdminEventProcess(EventAdminDTO eaDTO,
		                              @RequestParam(defaultValue="1") int currentPage,
		                              HttpSession session,
		                              Model model) {

		    Integer adminNum = (Integer) session.getAttribute("adminNum");
		    if (adminNum == null) {
		        return "redirect:/adminLogin";
		    }

		    eaDTO.setAdminNum(adminNum);

		    boolean flag = eas.addEvent(eaDTO);

		    model.addAttribute("flag", flag);
		    model.addAttribute("msg", flag ? "이벤트 추가 완료" : "이벤트 추가 실패");
		    model.addAttribute("redirectUrl", "/event/eventList?currentPage=" + currentPage);

		    return "/manager/event/eventResult";
		}

		
		
//		//이벤트 수정 메소드
		@PostMapping("/modifyEventProcess")
		public String modifyEventProcess(EventAdminDTO eaDTO,
		                                 @RequestParam(defaultValue="1") int currentPage,
		                                 HttpSession session,
		                                 Model model) {

		    Integer adminNum = (Integer) session.getAttribute("adminNum");
		    if (adminNum == null) {
		        return "redirect:/adminLogin";
		    }

		    eaDTO.setAdminNum(adminNum);

		    boolean flag = eas.modifyEvent(eaDTO);

		    model.addAttribute("flag", flag);
		    model.addAttribute("msg", flag ? "이벤트 수정 성공" : "이벤트 수정 실패");
		    model.addAttribute("redirectUrl", "/event/eventList?currentPage=" + currentPage);

		    return "/manager/event/eventResult";
		}



//		//이벤트 삭제 메소드
		@GetMapping("/removeEvent")
		public String removeEvent(@RequestParam int eventNum,
		                          @RequestParam(defaultValue="1") int currentPage,
		                          HttpSession session,
		                          Model model) {

		    Integer adminNum = (Integer) session.getAttribute("adminNum");
		    if (adminNum == null) {
		        return "redirect:/adminLogin";
		    }

		    EventAdminDTO dto = new EventAdminDTO();
		    dto.setEventNum(eventNum);
		    dto.setAdminNum(adminNum);

		    boolean flag = eas.removeEvent(dto);

		    model.addAttribute("flag", flag);
		    model.addAttribute("msg", flag ? "이벤트 삭제 성공" : "이벤트 삭제 실패");
		    model.addAttribute("redirectUrl", "/event/eventList?currentPage=" + currentPage);

		    return "/manager/event/eventResult";
		}


		
	
}

