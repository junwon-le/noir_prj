package kr.co.noir.reserve;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	public String roomResSearch(Model model) {
		List<String> roomDomain = new ArrayList<String>();
		roomDomain.add("디럭스");
		roomDomain.add("스위트");
		model.addAttribute("roomList",roomDomain);
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
}
