package kr.co.noir.login;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;


@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@GetMapping("/hotel/about")
	public String aboutHotel() {
		return "/hotel/about";
	}
	
	@GetMapping("/hotel/library")
	public String hotelLibrary() {
		return "/hotel/library";
	}

	@GetMapping("/hotel/gym")
	public String hotelGym() {
		return "/hotel/gym";
	}	

	@GetMapping("/hotel/laundry")
	public String hotelLaundry() {
		return "/hotel/laundry";
	}	
		
	@GetMapping("/login/memberLogin")
	public String memberLogin() {
		return "/login/memberLogin";
	}
	
	@GetMapping("/join")
	public String join() {
		return "/login/join";
	}
	
	@GetMapping("/findId") 
    public String findIdPage() {
        return "/login/findId";           // templates/findId.html 파일을 바라봄
    }

    @GetMapping("/findPw") 
    public String findPwPage() {
        return "/login/findPw";          // templates/findPw.html 파일을 바라봄
    }	

    /**
     * 로그아웃 처리
     */
    @GetMapping("/login/logout")
    public String logout(HttpSession session) {
        // 1. 세션 무효화 (저장된 member, admin 등 모든 속성 삭제)
        session.invalidate();
        
        // 2. 로그아웃 후 메인 페이지(또는 로그인 페이지)로 리다이렉트
        return "redirect:/main"; 
    }
	
	@PostMapping("/member/login")
	public String memberLoginProcess(@RequestParam("memberId") String memberId, @RequestParam("memberPass") String memberPass, HttpSession session) {
		
		MemberDTO member = memberService.login(memberId, memberPass);
		
		if (member != null) {
			// 로그인 성공: 세션에 member 정보 저장
			session.setAttribute("member", member);
			return "redirect:/main"; // 첫화면으로 
		} else {
			// 로그인 실패: 에러 파라미터와 함께 로그인 페이지로 리다이렉트
			return "redirect:/login/memberLogin?error=true";
		}//end if
		
	} //memberLoginProcess
	
	
	@PostMapping("/member/findId")
	public String findIdProcess(@RequestParam("memberName") String memberName, @RequestParam("memberEmail") String memberEmail, Model model) {
	    
	    // 1. 서비스에서 이름과 이메일로 ID 찾기 
	    // 예: select member_id from member where member_name = ? and member_email = ?
		MemberDTO mDTO=memberService.findIdByNameAndEmail(memberName, memberEmail);
		String foundId=mDTO.getMemberId();
	    
	    
	    if (foundId != null && !foundId.isEmpty()) {
	        // 2. 마스킹 처리 (앞 3자리 + 나머지는 *)
	        String maskedId = "";
	        
	        if (foundId.length() >= 3) {
	            maskedId = foundId.substring(0, 3) + "*******";
	        } else {
	            // ID가 3자리 미만인 경우(혹시 모를 예외 처리)
	            maskedId = foundId + "*******";
	        }
	        
	        // 3. 모델에 담아서 화면으로 보냄
	        model.addAttribute("searchResult", "success");
	        model.addAttribute("maskedId", maskedId);
	        
	    } else {
	        // 찾는 정보가 없을 경우
	        model.addAttribute("searchResult", "fail");
	    }
	    
	    // 다시 아이디 찾기 페이지(findId.html)로 돌아가서 스크립트를 실행
	    return "/login/findId";   

	}
	
	// 인증코드 검증
	@PostMapping("/verifyAuthCode")
	@ResponseBody
	public Map<String, Object> verifyAuthCode(@RequestParam String authCode, HttpSession session) {
	    Map<String, Object> response = new HashMap<>();
	    String serverCode = (String) session.getAttribute("authCode");

	    if (serverCode != null && serverCode.equals(authCode)) {
	        // 1. 보안 토큰 생성 (UUID)
	        String resetToken = UUID.randomUUID().toString();
	        
	        // 2. 세션에 토큰 저장 (비밀번호 변경 시 검증용)
	        session.setAttribute("resetToken", resetToken);
	        
	        response.put("result", "OK");
	        response.put("token", resetToken); // 클라이언트에 토큰 전달
	    } else {
	        response.put("result", "FAIL");
	    }
	    return response;
	}//verifyAuthCode

	// 비밀번호 재설정 페이지 이동 (토큰 검증)
	@GetMapping("/resetPw")
	public String resetPwPage(@RequestParam String token, HttpSession session, Model model) {
	    String sessionToken = (String) session.getAttribute("resetToken");

	    // 세션의 토큰과 URL의 토큰이 일치하는지 확인
	    if (sessionToken == null || !sessionToken.equals(token)) {
	        return "redirect:/member/findPw"; // 비정상 접근 시 차단
	    }
	    
	    model.addAttribute("token", token);
	    return "member/resetPw"; // 정상 접근 시 페이지 이동
	}//resetPwPage
	
	
	//비밀 번호 변경 
	@PostMapping("/updatePw")
	@ResponseBody
	public String updatePw(@RequestParam String newPw, 
	                       @RequestParam String token, 
	                       HttpSession session) {
	    String sessionToken = (String) session.getAttribute("resetToken");
	    String userId = (String) session.getAttribute("resetUserId");

	    // 최종 보안 검증
	    if (sessionToken == null || !sessionToken.equals(token) || userId == null) {
	        return "INVALID_ACCESS";
	    }

	    // 1. 비밀번호 암호화 및 DB 업데이트 (MyBatis 호출)
	    // boolean success = memberService.modifyPassword(userId, newPw);
	    boolean success = true; 

	    if (success) {
	        // 2. 사용 완료된 토큰 및 세션 정보 삭제 (재사용 방지)
	        session.removeAttribute("resetToken");
	        session.removeAttribute("authCode");
	        return "OK";
	    }
	    
	    return "FAIL";
	}//updatePw	
	
	
	@PostMapping("/modifyPwProcess")
	@ResponseBody
	public String modifyPwProcess(@RequestParam String newPw, @RequestParam String token, HttpSession session) {
	    
	    // 1. 세션에서 보안 정보 가져오기
	    String sessionToken = (String) session.getAttribute("resetToken");
	    String userId = (String) session.getAttribute("resetUserId");

	    // 2. 토큰 검증 (잘못된 접근 차단)
	    if (sessionToken == null || !sessionToken.equals(token) || userId == null) {
	        return "INVALID_ACCESS";
	    }

	    // 3. DB 업데이트 수행
	    boolean isSuccess = memberService.modifyPassword(userId, newPw);

	    if (isSuccess) {
	        // 4. 보안을 위해 변경 완료 후 관련 세션 즉시 제거
	        session.removeAttribute("resetToken");
	        session.removeAttribute("resetUserId");
	        session.removeAttribute("authCode");
	        return "OK";
	    } else {
	        return "FAIL";
	    }
	}//modifyPwProcess	
	
}//class