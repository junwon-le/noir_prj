package kr.co.noir.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // 비밀번호 암호화 의존성 추가
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // 알림창 메시지 전송용

import jakarta.servlet.http.HttpSession;

@RequestMapping("/mypage")
@Controller
public class MypageController {

    @Autowired
    private MypageService mps;
    
    @Autowired
    private PasswordEncoder passwordEncoder; // SecurityConfig에 등록된 Bean 주입

    // 마이페이지 메인
    @GetMapping("/")
    public String mypageView(HttpSession session, Model model) {
        String uri = "mypage/main"; // 뷰 경로 수정 (앞의 / 제거 권장)
        
        String memberId = (String) session.getAttribute("memberId");
        
        // 세션이 만료되었을 경우 로그인 페이지로
        if(memberId == null) {
            return "redirect:/login/memberLogin";
        }

        int hotelRevcnt = mps.searchHotelRevCnt(memberId);
        int dinningRevcnt = mps.searchDinningRevCnt(memberId);
        String name = mps.searchMemberName(memberId);
        List<EventDomain> eventList = mps.searchEventList();
        
        session.setAttribute("name", name);
        
        model.addAttribute("memberName", name);
        model.addAttribute("hotelRevCnt", hotelRevcnt);
        model.addAttribute("dinningRevcnt", dinningRevcnt);
        model.addAttribute("eventList", eventList);
        
        return uri;
    }
    
    /**
     * 회원 탈퇴 페이지 이동 (GET)
     */
    @GetMapping("/info/withdraw")
    public String withdrawPage() {
        return "mypage/withdraw"; // templates/mypage/withdraw.html
    }

    /**
     * 회원 탈퇴 처리 (POST)
     * - 일반회원: 비밀번호 검증 후 탈퇴
     * - SNS회원: 검증 없이 즉시 탈퇴
     */
//    @PostMapping("/info/removeMember")
    @PostMapping("/info/delete")    
    public String removeMember(@RequestParam(required = false) String currentPassword, 
                               HttpSession session, 
                               RedirectAttributes rttr) {
        
        String memberId = (String) session.getAttribute("memberId");
        String provider = (String) session.getAttribute("memberProvider"); // 'LOCAL', 'KAKAO' 등

        // 1. 일반 회원(LOCAL)일 경우 비밀번호 검증 수행
        if ("LOCAL".equals(provider)) {
            // DB에서 암호화된 비밀번호 가져오기 (Service에 메서드가 필요합니다)
            String dbPassword = mps.searchMemberPassword(memberId); 
            
            // 입력한 비밀번호와 DB 비밀번호 비교
            if (dbPassword == null || !passwordEncoder.matches(currentPassword, dbPassword)) {
                // 불일치 시: 메시지를 담아 다시 탈퇴 페이지로 리다이렉트
                rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다.");
                return "redirect:/mypage/info/memberLeave";
            }
        }

        // 2. 탈퇴 처리 (Service 호출)
        mps.withdrawMember(memberId); 
        
        // 3. 세션 초기화 (로그아웃)
        session.invalidate();
        
        // 4. 메인 페이지로 이동하면서 안내 메시지 전달 (선택사항)
        // rttr.addFlashAttribute("msg", "정상적으로 탈퇴되었습니다."); 
        return "redirect:/";
    }
    
}//class