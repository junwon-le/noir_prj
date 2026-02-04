package kr.co.noir.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/event")
@Controller
public class EventAdminController {
		
		@Autowired
		private EventAdminService eas;
	
//		
//		// 이벤트 목록 조회
//		@GetMapping("/searchEventList")
//		public searchEventList(Model , RangeDTO )
//
//		// 이벤트 상세 조회
//		@GetMapping("/searchEventDetail")
//		+ searchEventDetail(Model, eventNum: int) : String
//
//		// 이벤트 등록 페이지
//		@GetMapping("/addEventForm")
//		+addEventForm( ) : String
//
//		// 이벤트 등록 프로세스
//		@GetMapping("/addEventProcess")
//		+addEventProcess(EventDTO eDTO, HttpSession ) : String 
//		사진 두개는 멀티파트로 들어가고 다른 내용은 dto로
//		멀티파트에서 받은 파일명을 String으로 변환해서 dto로 넣어야 함
//
//		//이벤트 수정 메소드
//		@GetMapping("/modifyEventProcess")
//		+ modifyEventProcess( EventDTO : eDTO, HttpSession) : String
//
//		//이벤트 삭제 메소드
//		@GetMapping("/removeEvent")
//		+ removeEvent( eventNum: int) : String
//		
//	
}

