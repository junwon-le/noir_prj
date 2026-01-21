package kr.co.noir.dinning;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DinningController {

	@Autowired
	private DinningService ds;
	
	@GetMapping("/dinning")
	public String Dinning(Model model) {
		DinningDomain dDomain=null;
		dDomain=ds.searchDinning();
		
		model.addAttribute("dinning",dDomain);
		
		return "/dinning/dinning";
	}
	
	@GetMapping("/dinningDetail")
	public String DinningDetail(Model model) {
		List<DinningDomain> list=null;
		list=ds.searchDetailDinning();
		
		
		
		DinningDomain dDomain=list.get(0);
		
		
		model.addAttribute("dinning",dDomain);
		model.addAttribute("dinningList",list);
		
		
		
		
		return "/dinning/dinningDetail";
	}
	
}
