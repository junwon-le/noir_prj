package kr.co.noir.login;

import org.springframework.stereotype.Controller;


@Controller
public class MemberController {

//
//	@Controller
//	@RequestMapping("/admin")
//	public class MembersController {
//
//	    @Autowired
//	    private MemberService memberService; // 인터페이스 주입
//
//	    /**
//	     * 회원 정보 관리 페이지 (검색 및 페이징 기능 포함)
//	     */
//	    @GetMapping("/members")
//	    public String manageMembers(
//	            @RequestParam(value = "page", defaultValue = "1") int page,
//	            @RequestParam(value = "searchType", required = false) String searchType,
//	            @RequestParam(value = "keyword", required = false) String keyword,
//	            Model model) {
//
//	        // 1. 페이징 설정 (페이지당 10명)
//	        int size = 10;
//	        
//	        // 2. 검색 조건에 따른 목록 조회
//	        List<MemberDTO> memberList = memberService.getMemberPage(page, searchType, keyword);
//	        int totalMembers = memberService.getTotalCount(searchType, keyword);
//	        int totalPage = (int) Math.ceil((double) totalMembers / size);
//
//	        // 3. 뷰(HTML)로 데이터 전달
//	        model.addAttribute("memberList", memberList); // ${memberList}
//	        model.addAttribute("currentPage", page);      // ${currentPage}
//	        model.addAttribute("totalPage", totalPage);    // ${totalPage}
//	        model.addAttribute("searchType", searchType);  // ${searchType}
//	        model.addAttribute("keyword", keyword);        // ${keyword}
//
//	        return "manager/manageUser/manageMember"; // 에러 로그에 적힌 경로와 일치
//	    }
//
//	    /**
//	     * 선택 회원 탈퇴 처리 (AJAX 전송용)
//	     */
//	    @PostMapping("/members/delete")
//	    @ResponseBody
//	    public String deleteMembers(@RequestBody List<Integer> memberIds) {
//	        try {
//	            memberService.updateDelFlag(memberIds); // 탈퇴 플래그 'Y'로 변경
//	            return "OK";
//	        } catch (Exception e) {
//	            return "FAIL";
//	        }
//	    }
//	}
//	

}//class