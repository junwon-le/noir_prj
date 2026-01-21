package kr.co.noir.reserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.noir.room.RoomDomain;

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
	public String roomResSearch(RoomSearchDTO rsDTO, Model model) {
		
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
	
	@GetMapping("/RoomSearchProcess")
	@ResponseBody
	public List<RoomSearchDomain> roomSearchProcess(RoomSearchDTO rsDTO) {
		List<RoomSearchDomain> list = rrs.searchRoom(rsDTO);
		return list;
	}
}
