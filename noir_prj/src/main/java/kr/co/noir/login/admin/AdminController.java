package kr.co.noir.login.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.co.noir.dashboard.DashboardDTO;
import kr.co.noir.dashboard.DashboardService;
import kr.co.noir.login.MemberService;

@Controller
public class AdminController {

	@Autowired
    private DashboardService dashboardService;


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
    	DashboardDTO dashData = dashboardService.getDashboardData(); 
        
        // 2. HTML에서 사용하고 있는 "dash"라는 이름으로 모델에 추가합니다.
        model.addAttribute("dash", dashData); 
        
        return "manager/dashboard/dashBoard";

    }

    @Autowired
    private AdminMapper adminMapper; // MyBatis 사용 가정

    @Autowired
    private PasswordEncoder passwordEncoder; // SecurityConfig에 Bean 등록 필요    
    
    
    @PostMapping("/adminLogin")
    public String adminLogin(AdminDTO aDTO, HttpServletRequest request, HttpSession session, Model model) {
        
    		aDTO.setIp(request.getRemoteAddr());

    		// 1. DB에서 아이디로 관리자 정보 조회
            AdminDomain admin = adminMapper.selectAdmin(aDTO.getAdminId());

            System.out.println(admin);
            if (admin != null) {
            	
                // 2. 암호화된 비번 비교 (입력된 평문, DB의 암호문)
                if ((passwordEncoder.matches(aDTO.getAdminPass().trim(), admin.getAdminPass().trim()))) {
                    aDTO.setResult("S"); // 성공
                    aDTO.setAdminNum(admin.getAdminNum());
                    session.invalidate(); // 기존 세션 무효화 (보안)

                    HttpSession newSession = request.getSession(true);
                    newSession.setAttribute("adminId", admin.getAdminId());
                    newSession.setAttribute("adminNum", admin.getAdminNum());

                    // [중요] 뷰 이름을 리턴하는 게 아니라, 대시보드 URL로 '리다이렉트' 해야 함
                    return "redirect:/dashboard";
                    
                } else {
                    aDTO.setResult("F");
                    admin.setResultMsg("비밀번호가 일치하지 않습니다.");
                }
            } else {
                aDTO.setResult("F");
                admin = new AdminDomain(); // NPE 방지
                admin.setResultMsg("존재하지 않는 관리자 계정입니다.");
            }
    		return "login/adminLogin"; 
        }    		
    		
    		
//    		// 암호화 비교 로직이 적용된 서비스 호출
//    		AdminDomain ad = adminService.adminLogin(aDTO);
//    		
//    		if ("S".equals(aDTO.getResult())) {
//    			session.invalidate(); 
//    			HttpSession newSession = request.getSession(true);
//    			
//    			// 서비스에서 채워준 정보를 세션에 저장
//    			newSession.setAttribute("adminId", aDTO.getAdminId());            
//    			newSession.setAttribute("adminNum", aDTO.getAdminNum());            
//    			
//    			return "redirect:/admin/dashBoard"; 
//    		}
//    		
//    		model.addAttribute("errFlag", true);
//    		model.addAttribute("errMsg", ad.getResultMsg());
//    		return "login/adminLogin"; 
//    	}    

    	/*
	 * // 1. 접속 IP 설정 aDTO.setIp(request.getRemoteAddr());
	 * 
	 * // 2. 서비스 호출 (비밀번호 비교 및 결과 메시지 세팅 로직 포함) AdminDomain ad =
	 * adminService.adminLogin(aDTO);
	 * 
	 * // 3. 로그인 결과 처리 if ("S".equals(aDTO.getResult())) { // 관리자 로그인 성공하면 기존 세션을
	 * 무효화 함 session.invalidate();
	 * 
	 * // 새로운 세션을 생성합니다 (true를 주면 새 세션을 만듦) HttpSession newSession =
	 * request.getSession(true); // 새로운 세션 생성 newSession.setAttribute("adminId",
	 * aDTO.getAdminId()); newSession.setAttribute("adminNum", aDTO.getAdminNum());
	 * 
	 * return "redirect:/admin/dashBoard"; }
	 * 
	 * // [실패] 서비스에서 담아온 메시지를 그대로 화면에 전달 model.addAttribute("errFlag", true);
	 * model.addAttribute("errMsg", ad.getResultMsg());
	 * 
	 * return "adminLogin"; // 다시 로그인 페이지로 }
	 */    
    
    // 로그아웃 기능 
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션 무효화
        return "redirect:/login/adminLogin";
    }
    

    @Autowired
    private MemberService memberService;

    @GetMapping("/admin/members")
    public String listMembers(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(required = false) String searchType,
                              @RequestParam(required = false) String keyword,
                              Model model) {
        
        Map<String, Object> result = memberService.getMemberPage(page, searchType, keyword);
        
        model.addAttribute("memberList", result.get("memberList"));
        model.addAttribute("totalPage", result.get("totalPage"));
        model.addAttribute("currentPage", page);
        model.addAttribute("searchType", searchType);
        model.addAttribute("keyword", keyword);
        
        return "manager/manageUser/manageMember"; // HTML 파일명
    }

    @PostMapping("/admin/members/delete")
    @ResponseBody
    public String deleteMembers(@RequestBody List<String> memberIds) {
        if (memberService.removeMembers(memberIds)) {
            return "OK";
        }
        return "FAIL";
    }


    @GetMapping("/adminLogout")
    public String adminLogout(HttpServletRequest request, HttpServletResponse response) {
        // 1. 현재 로그인된 사용자의 인증 정보를 가져옴.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // 2. 인증 정보가 존재한다면 로그아웃 핸들러를 통해 로그아웃 처리함
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        // 3. 로그아웃 후 관리자 로그인 페이지로 리다이렉트한다.
        // 뒤에 ?logout 쿼리 파라미터를 붙여 로그인 페이지에서 "로그아웃 되었습니다" 메시지를 띄울 수 있음.
        return "redirect:/adminLogin";
    }
    
    
}//AdminController