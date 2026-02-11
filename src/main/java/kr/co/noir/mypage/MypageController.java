package kr.co.noir.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import kr.co.noir.event.EventDomain;

@RequestMapping("/mypage")
@Controller
public class MypageController {

    @Autowired
    private MypageService mps;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 마이페이지 메인
     */
    @GetMapping("/")
    public String mypageView(HttpSession session, Model model) {
        String memberId = (String) session.getAttribute("memberId");
        
        // 1. 세션 체크
        if(memberId == null) {
            return "redirect:/login/memberLogin";
        }

        // 2. 로그인 제공자 판별 (HEAD 로직 통합)
        // memberId가 'kakao_...' 또는 'google_...' 형태인 경우를 처리
        String memberProvider = "LOCAL";
        String[] memberSplit = memberId.split("_");
        if (memberSplit.length > 1) {
            String prefix = memberSplit[0].toUpperCase();
            if ("KAKAO".equals(prefix) || "GOOGLE".equals(prefix) || "NAVER".equals(prefix)) {
                memberProvider = prefix;
            }
        }
        // HTML에서 사용할 수 있도록 세션에 저장
        session.setAttribute("memberProvider", memberProvider);

        // 3. 데이터 조회
        int hotelRevcnt = mps.searchHotelRevCnt(memberId);
        int dinningRevcnt = mps.searchDinningRevCnt(memberId);
        String name = mps.searchMemberName(memberId);
        List<EventDomain> eventList = mps.searchEventList();
        
        session.setAttribute("name", name);
        
        model.addAttribute("memberName", name);
        model.addAttribute("hotelRevCnt", hotelRevcnt);
        model.addAttribute("dinningRevcnt", dinningRevcnt);
        model.addAttribute("eventList", eventList);
        
        return "mypage/main";
    }
    
    /**
     * 회원 탈퇴 페이지 이동 (GET)
     */
    /*
    @GetMapping("/info/memberLeave")
    public String withdrawPage(HttpSession session) {
        if(session.getAttribute("memberId") == null) {
            return "redirect:/login/memberLogin";
        }
        return "mypage/memberLeave";
    }
	*/


    /**
     * 회원 탈퇴 처리 (POST)
     * - 일반회원(LOCAL): 비밀번호 검증 후 탈퇴
     * - SNS회원: 검증 없이 즉시 탈퇴
     */
    @PostMapping("/info/delete")    
    public String removeMember(@RequestParam(required = false) String currentPassword, 
                               HttpSession session, 
                               RedirectAttributes rttr) {
        
        String memberId = (String) session.getAttribute("memberId");
        String provider = (String) session.getAttribute("memberProvider");

        // 1. 일반 회원(LOCAL)일 경우 비밀번호 검증 수행
        if ("LOCAL".equals(provider)) {
            String dbPassword = mps.searchMemberPassword(memberId); 
            
            if (dbPassword == null || !passwordEncoder.matches(currentPassword, dbPassword)) {
                rttr.addFlashAttribute("flag", true); // HTML의 flag 로직과 연동
                rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
                return "redirect:/mypage/info/memberLeave";
            }
        }

        // 2. 탈퇴 처리
        mps.withdrawMember(memberId); 
        
        // 3. 세션 초기화
        session.invalidate();
        
        rttr.addFlashAttribute("msg", "정상적으로 탈퇴되었습니다. 그동안 이용해 주셔서 감사합니다."); 
        return "redirect:/";
    }
}