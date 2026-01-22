package kr.co.noir.login;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
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
	
	@GetMapping("/join")
	public String join() {
		return "/login/join";
	}

	@GetMapping("/login/memberLogin")
	public String memberLogin() {
		return "/login/memberLogin";
	}
	
	@GetMapping("/login/findId") 
    public String findIdPage() {
        return "/login/findId";           // templates/findId.html 파일을 바라봄
    }

    @GetMapping("/login/findPw") 
    public String findPwPage() {
        return "login/findPw";          // templates/findPw.html 파일을 바라봄
    }	

    /**
     * 로그아웃 처리
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 1. 세션 무효화 (저장된 member, admin 등 모든 속성 삭제)
        session.invalidate();
        
        // 2. 로그아웃 후 메인 페이지(또는 로그인 페이지)로 리다이렉트
        return "redirect:/main"; 
    }
	
	@PostMapping("/login/memberLogin")
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
	
	
	@PostMapping("/login/findId")
	public String findIdProcess(@RequestParam("memberLastName") String memberLastName, @RequestParam("memberFirstName") String memberFirstName, 
								@RequestParam("memberEmail") String memberEmail, Model model) {
	    
	    // 1. 서비스에서 이름과 이메일로 ID 찾기 
	    // 예: select member_id from member where member_name = ? and member_email = ?
		String foundId=null;
		foundId=memberService.findIdByInfo(memberLastName, memberFirstName, memberEmail);
	    
	    
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
	

	@Autowired
	private MailService mailService; // 서비스 주입 확인

	@PostMapping("/login/sendAuthCode") // HTML의 AJAX 경로와 일치
	@ResponseBody
	public String sendAuthCode(@RequestParam("memberId") String memberId,
	                           @RequestParam("memberLastName") String memberLastName,
	                           @RequestParam("memberFirstName") String memberFirstName,
	                           @RequestParam("memberEmail") String memberEmail,
	                           HttpSession session) {

	    // 1. DB 사용자 정보 확인 (기존 서비스 활용)
	    boolean userExists = memberService.checkUserForPasswordReset(memberId, memberLastName, memberFirstName, memberEmail);

	    if (userExists) {
	        // 2. MailService의 함수명 'sendMail'을 정확히 호출
	        String authCode = mailService.sendMail(memberEmail);

	        if (authCode != null) {
	            // 3. 발송된 번호를 세션에 저장 (verifyAuthCode에서 확인용)
	            session.setAttribute("authCode", authCode);
	            session.setAttribute("resetMemberId", memberId); 
	            return "OK"; // HTML 스크립트의 res.trim() === "OK"와 일치
	        }
	        return "FAIL";
	    }
	    return "NO_USER";
	}	
	
	
	// 인증코드 검증
	@PostMapping("/login/verifyAuthCode")
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
	@GetMapping("/login/resetPw")
	public String resetPwPage(@RequestParam String token, HttpSession session, Model model) {
	    String sessionToken = (String) session.getAttribute("resetToken");

	    // 세션의 토큰과 URL의 토큰이 일치하는지 확인
	    if (sessionToken == null || !sessionToken.equals(token)) {
	        return "redirect:/login/resetPw"; // 비정상 접근 시 차단
	    }
	    
	    model.addAttribute("token", token);
	    return "login/resetPw"; // 정상 접근 시 페이지 이동
	}//resetPwPage
	
	
	//비밀 번호 변경 
	@PostMapping("/login/updatePw")
	@ResponseBody
	public String updatePw(@RequestParam String memberPass, 
	                       @RequestParam String token, 
	                       HttpSession session) {
	    String sessionToken = (String) session.getAttribute("resetToken");
	    String memberId = (String) session.getAttribute("resetMemberId");

	    // 최종 보안 검증
	    if (sessionToken == null || !sessionToken.equals(token) || memberId == null) {
	        return "INVALID_ACCESS";
	    }

	    // 1. 비밀번호 암호화 및 DB 업데이트 (MyBatis 호출)
	    boolean success = memberService.modifyPassword(memberId, memberPass);

	    if (success) {
	        // 2. 사용 완료된 토큰 및 세션 정보 삭제 (재사용 방지)
	        session.removeAttribute("resetToken");
	        session.removeAttribute("authCode");
	        return "OK";
	    }
	    
	    return "FAIL";
	}//updatePw	
	
	
	@PostMapping("/login/modifyPwProcess")
	@ResponseBody
	public String modifyPwProcess(@RequestParam String newPw, @RequestParam String token, HttpSession session) {
	    
	    // 1. 세션에서 보안 정보 가져오기
	    String sessionToken = (String) session.getAttribute("resetToken");
	    String memberId = (String) session.getAttribute("resetMemberId");

	    // 2. 토큰 검증 (잘못된 접근 차단)
	    if (sessionToken == null || !sessionToken.equals(token) || memberId == null) {
	        return "INVALID_ACCESS";
	    }

	    // 3. DB 업데이트 수행
	    boolean isSuccess = memberService.modifyPassword(memberId, newPw);

	    if (isSuccess) {
	        // 4. 보안을 위해 변경 완료 후 관련 세션 즉시 제거
	        session.removeAttribute("resetToken");
	        session.removeAttribute("resetMemberId");
	        session.removeAttribute("authCode");
	        return "OK";
	    } else {
	        return "FAIL";
	    }
	}//modifyPwProcess	
	

    @GetMapping("/login/checkId")
    public ResponseEntity<Map<String, Object>> checkId(@RequestParam("memberId") String memberId) {
        Map<String, Object> response = new HashMap<>();
        
        // 아이디 존재 여부 확인 (있으면 true, 없으면 false)
        boolean exists = memberService.existsById(memberId);
        
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
	
    // 회원가입 처리 프로세스
    @PostMapping("/login/joinProcess")
    @ResponseBody // 성공 여부를 JSON이나 문자열로 응답
    public String joinProcess(MemberDTO memberDTO, HttpServletRequest request) {
        // 1. 비밀번호 암호화 (추후 BCrypt 적용 권장)
        // 2. 성(lastName)과 이름(firstName)을 합쳐서 저장하거나 각각 저장하는 로직
        
        boolean isSuccess = memberService.registerMember(memberDTO, request);
        
        if(isSuccess) {
            return "OK";
        } else {
            return "FAIL";
        }
    }
    
	
}//class