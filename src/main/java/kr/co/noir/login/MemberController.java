package kr.co.noir.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

	
	
	
	
}