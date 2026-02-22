package kr.co.noir.review;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin/review")
public class ReviewAdminController {

    @Autowired
    private ReviewAdminService ras;

    private Integer getAdminNum(HttpSession session) {
        return (Integer) session.getAttribute("adminNum");
    }

    /* =========================
       1. 전체 목록
       ========================= */
    @GetMapping("/list")
    public String reviewAdminList(ReviewRangeDTO rrDTO, Model model, HttpSession session) {

        if (getAdminNum(session) == null)
            return "redirect:/admin/login";

        if (rrDTO.getCurrentPage() <= 0)
            rrDTO.setCurrentPage(1);

        rrDTO.setRoomTypeNum(0);
        rrDTO.setUrl("/admin/review/list");

        int totalCount = ras.totalCnt(rrDTO);
        int pageScale = ras.pageScale();
        int totalPage = ras.totalPage(totalCount, pageScale);

        rrDTO.setStartNum(ras.startNum(rrDTO.getCurrentPage(), pageScale));
        rrDTO.setEndNum(ras.endNum(rrDTO.getStartNum(), pageScale));
        rrDTO.setTotalPage(totalPage);

        List<ReviewAdminDomain> reviewList = ras.getReviewAdminList(rrDTO);

        model.addAttribute("reviewAllList", reviewList);
        model.addAttribute("pagination", ras.pagination(rrDTO, "center"));
        model.addAttribute("rrDTO", rrDTO);

        return "manager/review/reviewAdminList";
    }

    /* =========================
       2. 객실 필터 목록
       ========================= */
    @GetMapping("/adminRoomList")
    public String roomReviewAdminList(@RequestParam(defaultValue = "0") int num,
                                      ReviewRangeDTO rrDTO,
                                      Model model,
                                      HttpSession session) {

        if (getAdminNum(session) == null)
            return "redirect:/admin/login";

        if (rrDTO.getCurrentPage() <= 0)
            rrDTO.setCurrentPage(1);

        rrDTO.setRoomTypeNum(num);
        rrDTO.setUrl("/admin/review/adminRoomList");

        int totalCount = ras.totalCnt(rrDTO);
        int pageScale = ras.pageScale();
        int totalPage = ras.totalPage(totalCount, pageScale);

        rrDTO.setStartNum(ras.startNum(rrDTO.getCurrentPage(), pageScale));
        rrDTO.setEndNum(ras.endNum(rrDTO.getStartNum(), pageScale));
        rrDTO.setTotalPage(totalPage);

        List<ReviewAdminDomain> reviewList = ras.getReviewAdminList(rrDTO);

        model.addAttribute("reviewAllList", reviewList);
        model.addAttribute("pagination", ras.pagination(rrDTO, "center"));
        model.addAttribute("rrDTO", rrDTO);
        model.addAttribute("selectedRoomTypeNum", num);

        return "manager/review/reviewAdminList";
    }

    /* =========================
       3. 상세 JSON
       ========================= */
    @GetMapping("/detail-json")
    @ResponseBody
    public Map<String, Object> reviewDetailJson(@RequestParam int reviewNum,
                                                HttpSession session) {

        if (getAdminNum(session) == null)
            return Map.of("ok", false, "reason", "NO_SESSION");

        ReviewAdminDomain detail = ras.searchOneReview(reviewNum);
        List<String> imgList = ras.searchReviewImgList(reviewNum);

        return Map.of(
                "ok", true,
                "detail", detail,
                "imgList", imgList
        );
    }

    /* =========================
       4. 답변 등록
       ========================= */
    @PostMapping("/reply")
    public String reviewReplyProcess(ReviewAdminDTO raDTO,
                                     @RequestParam(defaultValue = "1") int currentPage,
                                     @RequestParam(defaultValue = "0") int roomTypeNum,
                                     HttpSession session,
                                     Model model) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null)
            return "redirect:/admin/login";

        raDTO.setAdminNum(adminNum);

        boolean flag = ras.replyReview(raDTO);

        model.addAttribute("flag", flag);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("roomTypeNum", roomTypeNum);

        return "manager/review/reviewReplyProcess";
    }

    /* =========================
       5. 리뷰 삭제
       ========================= */
    @PostMapping("/delete")
    public String reviewDelete(@RequestParam int reviewNum,
                               @RequestParam(defaultValue = "1") int currentPage,
                               @RequestParam(defaultValue = "0") int roomTypeNum,
                               HttpSession session,
                               Model model) {

        if (getAdminNum(session) == null)
            return "redirect:/admin/login";

        boolean flag = ras.removeReview(reviewNum);

        model.addAttribute("flag", flag);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("roomTypeNum", roomTypeNum);

        return "manager/review/reviewDeleteProcess";
    }

    /* =========================
       6. 답변만 삭제
       ========================= */
    @PostMapping("/delete-reply")
    public String reviewDeleteOnlyReply(@RequestParam int reviewNum,
                                        @RequestParam(defaultValue = "1") int currentPage,
                                        @RequestParam(defaultValue = "0") int roomTypeNum,
                                        HttpSession session,
                                        Model model) {

        if (getAdminNum(session) == null)
            return "redirect:/admin/login";

        boolean flag = ras.removeOnlyReply(reviewNum);

        model.addAttribute("flag", flag);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("roomTypeNum", roomTypeNum);

        return "manager/review/reviewDeleteOnlyReplyProcess";
    }
}