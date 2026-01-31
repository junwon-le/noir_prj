package kr.co.noir.reserve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/reserve")
@Controller
public class ReserveController {
	
	@Autowired
	private RoomReserveService rrs;

	
	@GetMapping("/roomReserve")
	public String reserve(HttpSession session,Model model) {
		//사용자 정보 가져오기
//		MemberDTO member = (MemberDTO)session.getAttribute("member");
//		String id= member.getMemberId();
		String id="user1";
		model.addAttribute("member",rrs.searchMember(id));
		
		return "/reserve/roomRes";
	}	
	
	@PostMapping("/pending")
	@ResponseBody
	public ResponseEntity<String> reserve(@RequestBody List<RoomDependingDTO> PdList) {
		//예약 객실을 보류테이블에 추가 
		try {
	        boolean flag = rrs.addRoomDepending(PdList);
	        if(flag) {
	        	return ResponseEntity.ok("success"); 
	        	
	        }else {
	        	return ResponseEntity.badRequest().body("fail");
	        }	        	
	        // 2. 성공 응답 보내기 (HTTP 200)
	        
	    } catch (Exception e) {
	        // 3. 실패 응답 보내기 (HTTP 400 또는 500)
	        // 이렇게 보내면 axios의 .catch() 블록이 실행됩니다.
	    	return ResponseEntity.badRequest().body("이미 예약된 객실이 포함되어 있습니다.");
	    }
	}
	
	@GetMapping("/nonRoomReserve")
	public String nonReserve() {
		return "/reserve/nonRoomRes";
	}
	
	@GetMapping("/roomResSearch")
	public String roomResSearch() {
		return "/reserve/roomResSearch";
	}
	
	@GetMapping("/dinningResSearch")
	public String dinningResSearch() {
		return "/reserve/dinningResSearch";
	}
	@GetMapping("/dinningRes")
	public String dinningRes() {
		return "/reserve/dinningRes";
	}
	@GetMapping("/nonDinningRes")
	public String nonDinningRes() {
		return "/reserve/nonDinningRes";
	}
	
	@GetMapping("/admin/nonRoomRes")
	public String adminNonRoomRes() {
		return "/manager/reserve/nonRoomRes";
	}
	@GetMapping("/admin/nonDinningRes")
	public String adminNonDinningRes() {
		return "/manager/reserve/nonDinningRes";
	}
	
	@PostMapping("/complete")
	public String reserveComplete(RoomReserveDTO rrDTO ,PayInfoDTO pDTO, HttpSession session, HttpServletRequest request, Model model) {
		String url ="/reserve/complete";
		//session id 가져오기
		//String id=session.getAttribute("userId");
		String id="user1";
		String ip= request.getRemoteAddr();
		//데이터 추가
		rrDTO.setReserve_ip(ip);
		rrDTO.setUser_id(id);
		rrDTO.setReserve_type("room");
		
		//예약 완료 시 테이블에 정보 추가
		boolean flag = rrs.addRoomReserve(pDTO,rrDTO);
		if(!flag) {
			url="reserve/err";
		}
		//예약 완료 시 보류 테이블에서 삭제 
		rrs.deleteDepending(id);
		
		return url;
	}
	
	@ResponseBody
	@GetMapping("/RoomSearchProcess")
	public List<RoomSearchDomain> roomSearchProcess(RoomSearchDTO rsDTO) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long period = 0;
		try {
			Date startDate= sdf.parse(rsDTO.getStartDate());
			Date endDate= sdf.parse(rsDTO.getEndDate());
			long diffInMillies = endDate.getTime() - startDate.getTime();
			period = diffInMillies / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<RoomSearchDomain> list = rrs.searchRoom(rsDTO);
		for(RoomSearchDomain rsd :list) {
			rsd.setPeriod(period);
		}
		return list;
	}
	
}
