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

import jakarta.servlet.http.HttpSession;

@RequestMapping("/dinningRes")
@Controller
public class DinningResController {

	
	@Autowired
	private DinningReserveService drs;

	
	@GetMapping("/dinningResSearch")
	public String dinningResSearch(Model model) {
		List<DinningMenuDomain> list = drs.SearchDinning();
		model.addAttribute("dinning", list);
		return "/reserve/dinningResSearch";
	}//dinningResSearch
	
	@GetMapping("/dinningReserve")
	public String dinningReserve(Model model) {
		return "/reserve/dinningRes";
	}//dinningReserve
	
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
			String JSessionId ="user1";
			ddDTO.setSessionId(JSessionId);
			System.out.println(ddDTO);
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
