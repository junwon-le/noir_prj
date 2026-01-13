package kr.co.noir.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("main")
@Controller
public class MainController {
	
	@GetMapping("test")
	public String Main() {
		return "test";
	}
}
