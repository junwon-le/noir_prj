package kr.co.noir.mypageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.co.noir.login.MemberDTO;

@RequestMapping("/mypage/info")
@Controller
public class MyPageInfoController {

	
	@Autowired
	MypageModifySevice mms;


//============================회원정보 수정================================	
	@GetMapping("/passwordCheck")
	public String passwoadCheck(HttpSession session) {
		return "/mypage/passwordCheck";
	}//passwoadCheck
	
	
	@PostMapping("/passwordCheckProcess")
	public String passwordCheckProcess(String userPw,HttpSession session,PasswordCheckDTO pcDTO,Model model) {
	
	    String userId = (String)session.getAttribute("memberId");

	    // 2. 기본 결과 페이지는 다시 입력 폼으로 설정
	    String uri = "/mypage/passwordCheck";
	    
	    pcDTO.setMemberid(userId);
	    pcDTO.setCurrentPassword(userPw);
	    
	    // 3. 비밀번호 검증 로직
	    if (mms.searchPasswordCheck(pcDTO)) {
	    	MemberInfoDomain miDomain = mms.searchMemmberInfo(userId);
	    	model.addAttribute("miDomain", miDomain);
	    	uri = "/mypage/memberModify"; // 맞으면 이동할 페이지 변경
	    } else {
	        model.addAttribute("flag", true); // 틀리면 경고용 플래그 전달
	    }
	    
	    return uri;
	}//passwordCheckProcess
	

	
	
	@PostMapping("/memberModityProcess")
	public String memberModityProcess(HttpSession session, MemberDTO mDTO,Model model) {
		mDTO.setMemberId((String)session.getAttribute("memberId"));
		String uri = "/mypage/memberModify";
		if(mms.modifyMemberInfo(mDTO)) {
			
			model.addAttribute("msg","회원정보 수정이 완료되었습니다.");
			uri="/mypage/successPage";
			
			
		}//end if
		
		return uri;
	}//memberModityProcess
	
	
//====================비밀번호 변경 =========================================
	
	@GetMapping("/passwordChangeFrm")	
	public String passwordModifyFrm(HttpSession session) {
		return "/mypage/passwordChange";
	}//passwordModifyFrm
	
	@PostMapping("/updatePassword")
	public String updatePassword(PasswordCheckDTO pcDTO,HttpSession session,Model model) {
		String uri="/mypage/passwordChange";
		boolean flag =false;
		pcDTO.setMemberid((String)session.getAttribute("memberId"));
		
		flag=mms.modifyPassword(pcDTO);
//		System.out.println(pcDTO);
		if(flag) {
			session.invalidate();
			model.addAttribute("msg","비밀번호 수정이 완료되었습니다.");
			return"/mypage/successPage";
		}else {
			model.addAttribute("flag",flag);
			model.addAttribute("pcDTO", pcDTO);
		}//end else
		
		 return uri;
	}//updatePassword
	
	
	
	
//=====================회원탈퇴============================
	
	@GetMapping("/memberLeave")
	public String memberLeaveView(HttpSession session) {
		
		return "/mypage/memberLeave";
		
	}//memberLeave
	
	@PostMapping("/removeMember")
	public String removeMember(PasswordCheckDTO pcDTO,HttpSession session,Model model) {
		
		
		String userId = (String)session.getAttribute("memberId");

	    // 2. 기본 결과 페이지는 다시 입력 폼으로 설정
	    String uri = "/mypage/memberLeave";
	    
	    pcDTO.setMemberid(userId);
	    
	    // 3. 비밀번호 검증 로직
	    if (mms.searchPasswordCheck(pcDTO)) {
	    	
	    	boolean flag=mms.removeMember(pcDTO);
	    	if(flag) {
	    		session.invalidate();
	    		model.addAttribute("msg","회원탈퇴가 정상적으로 완료되었습니다.");
	    		return"/mypage/successPage";
	    	}else {
	    		model.addAttribute("flag", true); // 틀리면 경고용 플래그 전달
	    	}//end else
	    } else {
	        model.addAttribute("flag", true); // 틀리면 경고용 플래그 전달
	    }//end else
	    
	    return uri;
	}//removeMember
}//class
