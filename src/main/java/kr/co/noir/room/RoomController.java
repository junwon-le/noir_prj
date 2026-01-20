package kr.co.noir.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoomController {

	@Autowired
	private RoomService rService;
	
	
	@GetMapping("/roomView")
	public String roomView(String num, Model model) {
		
		
		int number =  Integer.parseInt(num);
		model.addAttribute("room",rService.searchRoom(number));
		
		return "/room/room";
	}
	
	
	
	@GetMapping("/roomDetailView")
	public String roomDetailView() {
		return "/room/roomDetail";
	}
	
	
}
