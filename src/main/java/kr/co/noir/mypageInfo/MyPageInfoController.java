package kr.co.noir.mypageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import kr.co.noir.login.MemberDTO;

@RequestMapping("/mypage/info")
@Controller
public class MyPageInfoController {
	
	@Autowired
	MypageModifySevice mms;
	
	
	
	@GetMapping("/passwordCheck")
	public String passwoadCheck(HttpSession session) {
		
		String uri="redirect:/main";
		
		MemberDTO mDTO = (MemberDTO)session.getAttribute("member");
		if(mms.loginChk(mDTO)) {
			uri="/mypage/passwordCheck";
		}//end if
		
		return uri; 
	}//passwoadCheck
	
	
	@PostMapping("/passwordCheckProcess")
	public String passwordCheckProcess(String userPw,HttpSession session,PasswordCheckDTO pcDTO,Model model) {
	
		// 1. 세션 체크 (로그인 안 되어 있으면 즉시 로그인 페이지로)
	    MemberDTO mDTO = (MemberDTO)session.getAttribute("member");
	    if (mDTO == null) {
	    	return "redirect:/main";
	    }

	    // 2. 기본 결과 페이지는 다시 입력 폼으로 설정
	    String uri = "/mypage/passwordCheck";
	    
	    pcDTO.setMemberid(mDTO.getMemberId());
	    pcDTO.setCurrentPassword(userPw);
	    
	    // 3. 비밀번호 검증 로직
	    if (mms.searchPasswordCheck(pcDTO)) {
	    	uri = "/mypage/memberModify"; // 맞으면 이동할 페이지 변경
	    } else {
	        model.addAttribute("flag", true); // 틀리면 경고용 플래그 전달
	    }
	    
	    return uri;
	}//passwordCheckProcess
	
}//class
