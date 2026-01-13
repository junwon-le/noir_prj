package kr.co.noir.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
	@GetMapping("/main")
	public String Main() {
		return "/main";
	}
	
	@GetMapping("/room")
	public String Room() {
		return "/room/room";
	}
	@GetMapping("/roomDetail")
	public String RoomDetail() {
		return "/room/roomDetail";
	}
	
	@GetMapping("/dinning")
	public String Dinning() {
		return "/dinning/dinning";
	}
	@GetMapping("/dinningDetail")
	public String DinningDetail() {
		return "/dinning/dinningDetail";
	}
}
