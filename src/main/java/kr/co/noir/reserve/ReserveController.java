package kr.co.noir.reserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/reserve")
@Controller
public class ReserveController {
	
	@Autowired
	private RoomReserveService rrs;
	
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
		List<RoomSearchDomain> roomDomain = rrs.searchRoom();
		
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
