package kr.co.noir.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewAdminController {
	
	@Autowired //field 의존성 주입
	private ReviewAdminService ras;
	
	//세션 체크
	private Integer getAdminNum(HttpSession session) {
	    return (Integer) session.getAttribute("adminNum");
	}
	
	
	//전체 리뷰 목록
	@GetMapping("/reviewAdminList")
    public String reviewAdminList(ReviewRangeDTO rrDTO, Model model, HttpSession session) {
	   
		Integer adminNum = getAdminNum(session);
	    if (adminNum == null) {
	        return "redirect:/admin/login";
	    }

        // 기본값 세팅 (첫 진입 안정화)
        int currentPage = rrDTO.getCurrentPage();
        if (currentPage <= 0) {
            currentPage = 1;
            rrDTO.setCurrentPage(currentPage);
        }

        // ✅ 추가 (전체 목록이면 roomTypeNum 무조건 0으로 고정)
        rrDTO.setRoomTypeNum(0);
        
        int totalCount = ras.totalCnt(rrDTO);
        int pageScale = ras.pageScale();
        int totalPage = ras.totalPage(totalCount, pageScale);
        
        int startNum = ras.startNum(currentPage, pageScale);
        int endNum = ras.endNum(startNum, pageScale);
        
        rrDTO.setStartNum(startNum);
        rrDTO.setEndNum(endNum);
        rrDTO.setTotalPage(totalPage);

        //pagination2가 링크 만들 때 사용할 URL 세팅
        rrDTO.setUrl("/reviewAdminList");
        
//        rrDTO.setStartNum(ras.startNum(rrDTO.getCurrentPage(), pageScale));
//        rrDTO.setEndNum(ras.endNum(rrDTO.getStartNum(), pageScale));
//        rrDTO.setTotalPage(totalPage);

        List<ReviewAdminDomain> reviewAllList = ras.getReviewAdminDomains(rrDTO);
        
        model.addAttribute("reviewAllList", reviewAllList);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pageScale", pageScale);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pagination", ras.pagination2(rrDTO, "center"));
        model.addAttribute("rrDTO", rrDTO);
     
     return "manage/review/reviewAdminList";
    }


	// 객실 타입 선택
//	@GetMapping("/reviewRoomType")
//	+ reviewRoomType(Model, HttpSession) : String
    // ✅ 객실 타입 필터 목록
    @GetMapping("/roomReviewList")
    public String roomReviewList(
            @RequestParam(defaultValue = "0") int num,
            ReviewRangeDTO rrDTO, Model model, HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) {
            return "redirect:/admin/login";
        }

        int currentPage = rrDTO.getCurrentPage();
        if (currentPage <= 0) {
            currentPage = 1;
            rrDTO.setCurrentPage(currentPage);
        }

        rrDTO.setRoomTypeNum(num);
        rrDTO.setUrl("/roomReviewList");

        int totalCount = ras.totalCnt(rrDTO); // totalCnt가 roomTypeNum > 0이면 room cnt로 분기 중
        int pageScale = ras.pageScale();
        int totalPage = ras.totalPage(totalCount, pageScale);

        int startNum = ras.startNum(currentPage, pageScale);
        int endNum = ras.endNum(startNum, pageScale);

        rrDTO.setStartNum(startNum);
        rrDTO.setEndNum(endNum);
        rrDTO.setTotalPage(totalPage);

        List<ReviewAdminDomain> roomReviewList = ras.getReviewRoomList(rrDTO);

        model.addAttribute("reviewAllList", roomReviewList); // 화면 재사용이면 키 통일
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pageScale", pageScale);
        model.addAttribute("totalPage", totalPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pagination", ras.pagination2(rrDTO, "center"));
        model.addAttribute("rrDTO", rrDTO);
        model.addAttribute("selectedRoomTypeNum", num);

        return "manage/review/reviewAdminList";
    }

//	// 리뷰 상세 보기
//	@GetMapping("/reviewDetailView")
//	+ reviewDetailView(Model, reviewNum : int ) : String
    // ✅ 리뷰 상세 보기 (+ 이미지 리스트 같이)
    @GetMapping("/reviewDetailView")
    public String reviewDetailView(@RequestParam int reviewNum,
                                   Model model, HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) {
            return "redirect:/admin/login";
        }

        ReviewAdminDomain detail = ras.searchOneReview(reviewNum);
        List<String> imgList = ras.searchReviewImgList(reviewNum); // 서비스에 추가해둔 메소드

        model.addAttribute("detail", detail);
        model.addAttribute("imgList", imgList);

        return "manage/review/reviewDetailView";
    }

	// 리뷰 답변 //리뷰 수정
//	@GetMapping("/reviewReply")
//	@GetMapping("/reviewModify")
//	+ reviewModify(EventDTO : eDTO, HttpSession) : String
//	+ reviewReply(Model, HttpSession) : String
   // ✅ 리뷰 답변 등록/수정 처리 (POST 추천)
    @PostMapping("/reviewReplyProcess")
    public String reviewReplyProcess(ReviewAdminDTO raDTO, HttpSession session, Model model) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) {
            return "redirect:/admin/login";
        }

        raDTO.setAdminNum(adminNum);

        boolean flag = ras.replyReview(raDTO); // 서비스에 추가해둔 메소드
        model.addAttribute("flag", flag);
        model.addAttribute("reviewNum", raDTO.getReviewNum());

        return "manage/review/reviewReplyProcess";
    }



	//해당 리뷰 전체 삭제
//	@GetMapping("/reviewDelete")
//	+ reviewDelete(Model, HttpSession) : String
    @PostMapping("/reviewDelete")
    public String reviewDelete(@RequestParam int reviewNum,
                               @RequestParam(defaultValue="1") int currentPage,
                               @RequestParam(defaultValue="0") int roomTypeNum,
                               HttpSession session, Model model) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) {
            return "redirect:/admin/login";
        }

        boolean flag = ras.removeReview(reviewNum);
        model.addAttribute("flag", flag);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("roomTypeNum", roomTypeNum);

        
        return "manage/review/reviewDeleteProcess";
    }

    //  답변만 삭제
    @PostMapping("/reviewDeleteOnlyReply")
    public String reviewDeleteOnlyReply(@RequestParam int reviewNum,
                                        HttpSession session, Model model) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) {
            return "redirect:/admin/login";
        }

        boolean flag = ras.removeOnlyReply(reviewNum);
        model.addAttribute("flag", flag);
        model.addAttribute("reviewNum", reviewNum);

        return "manage/review/reviewDeleteOnlyReplyProcess";
    }
	


}//class
