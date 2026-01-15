package kr.co.noir.reserve;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/reserve")
@Controller
public class ReserveController {
	@GetMapping("/roomReserve")
	public String reserve() {
		return "/reserve/roomRes";
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
}
