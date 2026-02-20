	package kr.co.noir.dinningReserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.noir.reserve.MemberDomain;
import kr.co.noir.reserve.PayInfoDTO;
import kr.co.noir.reserve.RoomReserveService;

@RequestMapping("/dinningRes")
@Controller
public class DinningResController {

	
	@Autowired
	private DinningReserveService drs;
	
	@Autowired
	private RoomReserveService rrs;

	
	@GetMapping("/dinningResSearch")
	public String dinningResSearch(Model model) {
		List<DinningMenuDomain> list = drs.SearchDinning();
		model.addAttribute("dinning", list);
		return "reserve/dinningResSearch";
	}//dinningResSearch
	
	@GetMapping("/dinningReserve")
	public String dinningReserve(HttpSession session ,Model model) {
		String id = String.valueOf(session.getAttribute("memberId"));
		MemberDomain mDomain = rrs.searchMember(id);
		System.out.println(mDomain);
		model.addAttribute("memberDomain",mDomain);
		
		return "reserve/dinningRes";
	}//dinningReserve
	
	@GetMapping("/nonDinningReserve")
	public String nonDinningReserve() {
		
		return "reserve/nonDinningRes";
	}//dinningReserve
	
	@PostMapping("/complete")
	public String reserveComplete(DinningReserveDTO drDTO ,PayInfoDTO pDTO, HttpSession session, HttpServletRequest request, Model model) {
		String url ="reserve/dinningComplete";
		//session id 가져오기
		String id = String.valueOf(session.getAttribute("memberId")) ;
		//String id=session.getAttribute("userId");
		//String id="user1";
		String ip= request.getRemoteAddr();
		//데이터 추가
		drDTO.setReserve_ip(ip);
		drDTO.setUser_id(id);
		drDTO.setReserve_type("dinning");
		
		//예약 완료 시 테이블에 정보 추가
		boolean flag = drs.addDinningReserve(drDTO,pDTO);
		if(!flag) {
			url="reserve/err";
		}
		model.addAttribute("reserve" , drDTO);
		model.addAttribute("pay" , pDTO);
		//예약 완료 시 보류 테이블에서 삭제 
		drs.removeDepending(id);
		
		return url;
	}//reserveComplete
	@PostMapping("/nonComplete")
	public String reserveNonComplete(DinningReserveDTO drDTO ,PayInfoDTO pDTO, HttpSession session, HttpServletRequest request, Model model) {
		String url ="main";
		//session id 가져오기
		String id = session.getId() ;
		String ip= request.getRemoteAddr();
		//데이터 추가
		drDTO.setReserve_ip(ip);
		drDTO.setUser_id(id);
		drDTO.setReserve_type("dinning");
		
		//예약 완료 시 테이블에 정보 추가
		boolean flag = drs.addNonDinningReserve(drDTO,pDTO);
		if(!flag) {
			url="reserve/err";
		}
		model.addAttribute("reserve" , drDTO);
		model.addAttribute("pay" , pDTO);
		//예약 완료 시 보류 테이블에서 삭제 
		drs.removeDepending(id);
		
		return url;
	}//reserveComplete
	
	@ResponseBody
	@GetMapping("/dinningTime/{dinningType}/{dinningDate}")
	public List<DinningSearchDomain> dinningTimeSearch(@PathVariable String dinningType,@PathVariable String dinningDate) {
		DinningSearchDTO ddDTO = new DinningSearchDTO();
		ddDTO.setDinningDate(dinningDate);
		ddDTO.setDinningType(dinningType);
		System.out.println(ddDTO);
		List<DinningSearchDomain> list = drs.SearchDinningTime(ddDTO);
		return list;
	}//dinningTimeSearch
	
	@PostMapping("/depending")
	@ResponseBody
	public ResponseEntity<String> reserve(@RequestBody DinningDependingDTO ddDTO , HttpSession session) {		//예약 객실을 보류테이블에 추가 
		try {
			String id =session.getId();
			if(session.getAttribute("memberId")!=null){
				id =String.valueOf(session.getAttribute("memberId"));
			}
			ddDTO.setSessionId(id);
	        boolean flag = drs.addDepending(ddDTO);
	        if(flag) {
	        	return ResponseEntity.ok("success"); 
	        }else {
	        	return ResponseEntity.badRequest().body("fail");
	        }//end else	        	
	        // 2. 성공 응답 보내기 (HTTP 200)
	        
	    } catch (Exception e) {
	        // 3. 실패 응답 보내기 (HTTP 400 또는 500)
	        // 이렇게 보내면 axios의 .catch() 블록이 실행됩니다.
	    	return ResponseEntity.badRequest().body("이미 예약중인 내역이 있습니다.");
	    }
	}
}
