package kr.co.noir.neighbors;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class neighborsController {

	@GetMapping("/shopping")
	public String shopping() {		
		return "neighbors/shopping";
	}
	
	@GetMapping("/tourAttraction")
	public String tourAttraction() {		
		return "neighbors/tourAttraction";
	}
	
	@GetMapping("/culture")
	public String culture() {		
		return "neighbors/culture";
	}
	
	
}
