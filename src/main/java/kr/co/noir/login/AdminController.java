package kr.co.noir.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;


    @GetMapping("/dashboard")
    public String dashboard() {
        return "adminDashboard";
    }

    @PostMapping("/adminLogin")
    public String loginProcess(AdminDTO aDTO, HttpServletRequest request, HttpSession session, Model model) {
        
        // 1. 접속 IP 설정
        aDTO.setIp(request.getRemoteAddr());
        
        // 2. 서비스 호출 (비밀번호 비교 및 결과 메시지 세팅 로직 포함)
        AdminDomain ad = adminService.adminLogin(aDTO);
        
        // 3. 로그인 결과 처리
        if ("S".equals(aDTO.getResult())) {
            // [성공] 관리자 전용 세션 키 사용 권장
            session.setAttribute("adminId", aDTO.getAdminId());
            // 성공 시 관리자 메인(대시보드)으로 리다이렉트 -- 나중에 작업
//            return "redirect:/admin/dashboard"; 
            return "redirect:/"; // 이건 임시
        }
        
        // [실패] 서비스에서 담아온 메시지를 그대로 화면에 전달
        model.addAttribute("errFlag", true);
        model.addAttribute("errMsg", ad.getResultMsg());
        
        return "adminLogin"; // 다시 로그인 페이지로
    }
    
    // 로그아웃 기능 
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/adminLogin";
    }
}