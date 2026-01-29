package kr.co.noir.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;
	
	@GetMapping("/memberMgr")
	public String memberManager() {
		return "manager/manageUser/manageMember";
	}

	@GetMapping("/nonMemberMgr")
	public String nonMemberManager() {
		return "manager/manageUser/manageNonMember";
	}
    
    @GetMapping("/dashBoard")
    public String dashboard(Model model) {
        // 1. DB에서 데이터 조회
        DashboardDTO data = dashboardService.getDashboardData();
        
        // 2. 모델에 담기
        model.addAttribute("dash", data);
        
        return "manager/dashboard/dashBoard"; // dashboard.html 경로
    }
    
}//DashboardController