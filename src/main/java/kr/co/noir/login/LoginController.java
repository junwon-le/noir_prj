package kr.co.noir.login;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;



@RequestMapping("/login")
@Controller
public class LoginController {

	@Autowired
	private LoginService ls;

	@GetMapping("/memberLogin")
	public String memberLogin() {
		return "login/memberLogin";
	}
    
	@PostMapping("/memberLogin")
	public String login(LoginDTO lDTO, HttpServletRequest request, HttpSession session, Model model) {

		String url="login/memberLogin"; // 실패 
		
		lDTO.setMemberIp(request.getRemoteAddr());
		LoginMemberDomain md=ls.searchOneMember(lDTO);
		
		// 로그인 성공 S
		if( "S".equals(lDTO.getResult()) ) {
			
			// 1. null 체크를 포함한 이름 가공
		    String lastName = (md.getMemberLastName() != null) ? md.getMemberLastName().trim() : "";
		    String firstName = (md.getMemberFirstName() != null) ? md.getMemberFirstName().trim() : "";
		    String fullName = lastName + firstName;

		    session.setAttribute("memberId", lDTO.getMemberId());
		    session.setAttribute("memberName", fullName);
			
			url="redirect:/"; //성공하면 메인
		}
		
		model.addAttribute("errFlag",lDTO.getResult()!= null); //null이나 F
		model.addAttribute("errMsg",lDTO.getResult()==null?
				"아이디가 존재하지 않습니다" :"비밀번호가 일치하지 않습니다"); //null이나 F
		
		
		return url;
	}
	
	
	
	
	
	@GetMapping("/findId") 
    public String findIdPage() {
        return "login/findId";           // templates/findId.html 파일을 바라봄
    }

    @GetMapping("/findPw") 
    public String findPwPage() {
        return "login/findPw";          // templates/findPw.html 파일을 바라봄
    }	

    
	@Value("${user.crypto.key}")
	private String key;
	@Value("${user.crypto.salt}")
	private String salt;	    
    
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
	

	
	
	@PostMapping("/findId")
	public String findIdProcess(@RequestParam("memberLastName") String memberLastName, @RequestParam("memberFirstName") String memberFirstName, 
								@RequestParam("memberEmail") String memberEmail, Model model) {
	    
	    // 1. 서비스에서 이름과 이메일로 ID 찾기 
	    // 예: select member_id from member where member_name = ? and member_email = ?
		String foundId=null;
		foundId=ls.findIdByInfo(memberLastName, memberFirstName, memberEmail);
	    
	    
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

	@PostMapping("/sendAuthCode") // HTML의 AJAX 경로와 일치
	@ResponseBody
	public String sendAuthCode(@RequestParam("memberId") String memberId,
	                           @RequestParam("memberLastName") String memberLastName,
	                           @RequestParam("memberFirstName") String memberFirstName,
	                           @RequestParam("memberEmail") String memberEmail,
	                           HttpSession session) {

	// 1. 서비스에서 아이디, 이름, 이메일이 모두 일치하는지 확인 (이미 내부에서 복호화 비교 완료)
	   boolean isUserValid = ls.checkUserForPasswordReset(memberId, memberLastName, memberFirstName, memberEmail);
	   
	   System.out.println(isUserValid);
	   
	   // 2. 검증 통과 (chk가 true) -> 메일 발송
	   if (isUserValid) {
            // 입력받은 이메일(memberEmail)이 맞다는 것이 증명되었으므로 바로 전송
	        String authCode = mailService.sendMail(memberEmail);
	
	        if (authCode != null) {
	            session.setAttribute("authCode", authCode);
	            session.setAttribute("resetMemberId", memberId); 
	            return "OK"; // 성공
	        }
            return "FAIL"; // 메일 서버 오류 등으로 발송 실패
	   }
       
	   return "NO_USER"; // 회원 정보가 틀림
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
	        return "redirect:/login/resetPw"; // 비정상 접근 시 차단
	    }
	    
	    model.addAttribute("token", token);
	    return "login/resetPw"; // 정상 접근 시 페이지 이동
	}//resetPwPage
	
	
	//비밀 번호 변경 
	@PostMapping("/modifyPwProcess")
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
	    boolean isSuccess = ls.modifyPassword(memberId, newPw);

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
	

    @GetMapping("/checkId")
    public ResponseEntity<Map<String, Object>> checkId(@RequestParam("memberId") String memberId) {
        Map<String, Object> response = new HashMap<>();
        
        // 아이디 존재 여부 확인 (있으면 true, 없으면 false)
        boolean exists = ls.existsById(memberId);
        
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
	
    // 회원가입 처리 프로세스
    @PostMapping("/joinProcess")
    @ResponseBody // 성공 여부를 JSON이나 문자열로 응답
    public String joinProcess(MemberDTO memberDTO, HttpServletRequest request) {

    	String telPattern = "^010-\\d{4}-\\d{4}$";
        if (!memberDTO.getMemberTel().matches(telPattern)) {
            // 에러 처리: 다시 가입 폼으로 리턴
            return "redirect:/join?error=tel";
        }    	
    	
        // 1. 비밀번호 암호화 (BCrypt 적용)
        // 2. 성(lastName)과 이름(firstName)을 합쳐서 저장하거나 각각 저장하는 로직
        
        boolean isSuccess = ls.registerMember(memberDTO, request);

        System.out.println(isSuccess);
        
        if(isSuccess) {
            return "OK";
        } else {
            return "FAIL";
        }
        
    }
    
    @GetMapping("/result")
    public String showResult(
            @RequestParam(value = "message", required = false, defaultValue = "완료되었습니다.") String message,
            @RequestParam(value = "url", required = false, defaultValue = "/") String url,
            @RequestParam(value = "btnText", required = false, defaultValue = "메인 페이지") String btnText,
            Model model) {
        
        // 타임리프 변수 ${message}, ${url}, ${btnText}에 값을 매핑합니다.
        model.addAttribute("message", message);
        model.addAttribute("url", url);
        model.addAttribute("btnText", btnText);
        
        return "login/result"; // result.html 을 호출
    }
    
    /**
     * SNS 로그인 성공 후 리다이렉트되는 경로
     * Spring Security 설정에서 successHandler를 통해 이쪽으로 보낼 수 있음.
     */
    @GetMapping("/oauth2/code/{registrationId}")
    public String oauthLoginSuccess(@PathVariable String registrationId, 
                                    @AuthenticationPrincipal OAuth2User oAuth2User,
                                    Model model) {
        
        // OAuth2User를 통해 로그인한 유저의 정보를 가져옵니다.
        // registrationId(google, kakao, naver)에 따라 데이터 구조가 다르므로 처리가 필요합니다.
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        // 
        return "redirect:/"; 
    }    
    
    
    
	
}//class