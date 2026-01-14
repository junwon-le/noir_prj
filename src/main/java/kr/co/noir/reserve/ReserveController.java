package kr.co.noir.reserve;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/reserve")
@Controller
public class ReserveController {
	@GetMapping("/roomReserve.do")
	public String reserve() {
		return "/reserve/roomRes";
	}
}
