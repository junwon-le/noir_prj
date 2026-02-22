package kr.co.noir.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@Controller
public class MemberController {

	    @Autowired
	    private MemberService memberService;

	    /**
	     * 회원 탈퇴 처리
	     * POST 방식으로 요청을 받아 DB의 토큰 정보를 이용해 SNS 연동 해제 후 탈퇴를 진행
	     */
	    @PostMapping("/member/withdraw") 
	    public String withdraw(HttpSession session, 
	                           HttpServletRequest request, 
	                           HttpServletResponse response, 
	                           @RequestParam(value="currentPassword", required=false) String currentPassword,
	                           RedirectAttributes rttr) {
	    	
	        // 1. 세션에서 loginUser 객체 전체를 가져옵니다.
	        String memberId = (String) session.getAttribute("memberId");
	        String memberProvider = (String) session.getAttribute("memberProvider");
	        
	        // 로그인이 안 되어 있다면
	        if (memberId == null) {
	            rttr.addFlashAttribute("msg", "로그인 세션이 만료되었습니다.");
	            return "redirect:/login/memberLogin";
	        }
	        
	        try {
	            boolean isSuccess = false;

	            if ("LOCAL".equalsIgnoreCase(memberProvider)) {
	                // [일반 회원] 1. 비밀번호 먼저 검증
//	                boolean isPwMatch = memberService.checkPassword(loginUser.getMemberNum(), currentPassword);
//	                if (!isPwMatch) {
//	                    rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
//	                    return "redirect:/mypage/info/removeMemberView"; // 다시 탈퇴 페이지로
//	                }
	                // 2. 검증 성공 시 DB 삭제(또는 플래그 변경)
	                isSuccess = memberService.withdrawMember(memberId);
	            } else {
	                // [SNS 회원 로직]
	                isSuccess = memberService.processWithdrawal(memberId, memberProvider);
	            }

	            // 3. 탈퇴 로직이 성공했을 때만 로그아웃 및 세션 종료 실행
	            System.out.println(" 탈퇴 처리 : " + isSuccess);
	            if (isSuccess) {
	                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	                if (auth != null) {
	                    new SecurityContextLogoutHandler().logout(request, response, auth);
	                }

	                // 시큐리티 컨텍스트 홀더를 완전히 비움
	                SecurityContextHolder.clearContext();
	                
	                if (request.getSession(false) != null) {
	                    session.invalidate();
	                }
	                
	                
	                rttr.addFlashAttribute("msg", "회원 탈퇴가 정상적으로 처리되었습니다.");
	                return "redirect:/login/memberLogin";
	                
	            } else {
	                rttr.addFlashAttribute("msg", "탈퇴 처리 중 오류가 발생했습니다.");
	                return "redirect:/mypage/";
	            }

	        } catch (Exception e) {
	            e.printStackTrace(); // 여기서 NullPointerException이 났었을 확률이 높습니다.
	            rttr.addFlashAttribute("msg", "시스템 오류로 탈퇴가 중단되었습니다.");
	            return "redirect:/mypage/";
	        }
	    }
	    
}//class