package kr.co.noir.notice;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/notice")
public class NoticeAdminController {

    @Autowired
    private NoticeAdminService nas;

    /** =========================
     * ✅ 세션 보정형 adminNum 조회
     * ========================= */
    private Integer getAdminNum(HttpSession session) {

        Object v = session.getAttribute("adminNum");
        Integer adminNum = null;

        if (v instanceof Integer) {
            adminNum = (Integer) v;
        } else if (v instanceof String) {
            try { adminNum = Integer.parseInt((String) v); }
            catch (NumberFormatException e) { adminNum = null; }
        }

        if (adminNum != null && adminNum > 0) return adminNum;

        String adminId = (String) session.getAttribute("adminId");
        if (adminId == null || adminId.trim().isEmpty()) return null;

        Integer found = nas.findAdminNumByAdminId(adminId.trim());
        if (found != null && found > 0) {
            session.setAttribute("adminNum", found);
            return found;
        }
        return null;
    }

    private String enc(String s) {
        return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8);
    }

    /* =========================
       1) 공지사항 목록 (검색/필터/페이징)
       ========================= */
    @GetMapping("/noticeAdminList")
    public String adminNoticeList(BoardRangeDTO rDTO, Model model, HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        // 1) currentPage 기본값
        int currentPage = rDTO.getCurrentPage();
        if (currentPage <= 0) currentPage = 1;
        rDTO.setCurrentPage(currentPage);

        // 2) field/keyword 기본값
        if (rDTO.getField() == null || rDTO.getField().trim().isEmpty()) rDTO.setField("1");
        if (rDTO.getKeyword() == null) rDTO.setKeyword("");

        // ✅ 디버그 로그 (필터/검색 파라미터 확인용)
        System.out.println("[noticeAdminList] field=" + rDTO.getField()
                + ", keyword=" + rDTO.getKeyword()
                + ", currentPage=" + rDTO.getCurrentPage());

        // 3) totalCnt/pageScale/totalPage
        int totalCnt = nas.totalCnt(rDTO);
        int pageScale = nas.pageScale();
        int totalPage = nas.totalPage(totalCnt, pageScale);

        if (totalPage <= 0) totalPage = 1;

        // ✅ currentPage 보정 (초과하면 1페이지로)
        if (currentPage > totalPage) {
            currentPage = 1;
            rDTO.setCurrentPage(1);
        }

        rDTO.setTotalPage(totalPage);

        // 4) start/end 계산
        int startNum = nas.startNum(currentPage, pageScale);
        int endNum = nas.endNum(startNum, pageScale);

        rDTO.setStartNum(startNum);
        rDTO.setEndNum(endNum);
        rDTO.setUrl("/notice/noticeAdminList");

        // 5) 목록 조회 + 페이징
        List<NoticeAdminDomain> noticeList = nas.searchAdminNoticeList(rDTO);
        String pagination = nas.pagination2(rDTO, "center");

        // 화면 번호(listNum)
        int listNum = totalCnt - (currentPage - 1) * pageScale;
        if (listNum < 0) listNum = 0;

        model.addAttribute("listNum", listNum);
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("pagination", pagination);
        model.addAttribute("rDTO", rDTO);

        System.out.println("[" + rDTO.getField() + "]");
        return "manager/notice/noticeAdminList";
    }

    /* =========================
       2) 공지 상세 (선택)
       ========================= */
    @GetMapping("/noticeDetailAdmin")
    public String noticeDetail(@RequestParam int noticeNum,
                               @RequestParam(defaultValue = "1") int currentPage,
                               @RequestParam(defaultValue = "1") String field,
                               @RequestParam(defaultValue = "") String keyword,
                               Model model,
                               HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        model.addAttribute("notice", nas.searchOneNotice(noticeNum));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("field", field);
        model.addAttribute("keyword", keyword);

        return "manager/notice/noticeDetailAdmin";
    }

    /* =========================
       3) 공지 작성 폼
       ========================= */
    @GetMapping("/writeNoticeFrm")
    public String writeNoticeForm(@RequestParam(defaultValue = "1") int currentPage,
                                  @RequestParam(defaultValue = "1") String field,
                                  @RequestParam(defaultValue = "") String keyword,
                                  Model model,
                                  HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("field", field);
        model.addAttribute("keyword", keyword);

        return "manager/notice/noticeWriteFrmAdmin";
    }

    /* =========================
       4) 공지 작성 처리
       ========================= */
    @PostMapping("/noticeWriteProcess")
    public String writeNoticeProcess(NoticeAdminDTO naDTO,
                                     @RequestParam(defaultValue = "1") int currentPage,
                                     @RequestParam(defaultValue = "1") String field,
                                     @RequestParam(defaultValue = "") String keyword,
                                     RedirectAttributes ra,
                                     HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) {
            ra.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/admin/login";
        }

        naDTO.setAdminNum(adminNum);

        int cnt = nas.addAdminNotice(naDTO);

        ra.addFlashAttribute("msg",
                cnt == 1 ? "공지사항이 등록되었습니다." : "공지사항 등록에 실패했습니다.");

        if (cnt == 1) {
            return "redirect:/notice/noticeAdminList?currentPage=" + currentPage
                    + "&field=" + enc(field)
                    + "&keyword=" + enc(keyword);
        } else {
            return "redirect:/notice/writeNoticeFrm?currentPage=" + currentPage
                    + "&field=" + enc(field)
                    + "&keyword=" + enc(keyword);
        }
    }

    /* =========================
       5) 공지 수정 폼
       ========================= */
    @GetMapping("/modifyNoticeFrm")
    public String modifyNoticeForm(@RequestParam int noticeNum,
                                   @RequestParam(defaultValue = "1") int currentPage,
                                   @RequestParam(defaultValue = "1") String field,
                                   @RequestParam(defaultValue = "") String keyword,
                                   Model model,
                                   HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        model.addAttribute("notice", nas.searchOneNotice(noticeNum));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("field", field);
        model.addAttribute("keyword", keyword);

        return "manager/notice/noticeWriteFrmAdmin";
    }

    /* =========================
       6) 공지 수정 처리
       ========================= */
    @PostMapping("/modifyNoticeProcess")
    public String modifyNoticeProcess(NoticeAdminDTO naDTO,
                                      @RequestParam(defaultValue = "1") int currentPage,
                                      @RequestParam(defaultValue = "1") String field,
                                      @RequestParam(defaultValue = "") String keyword,
                                      RedirectAttributes ra,
                                      HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        naDTO.setAdminNum(adminNum);

        int cnt = nas.modifyNotice(naDTO);

        ra.addFlashAttribute("msg", cnt == 1 ? "수정되었습니다." : "수정에 실패했습니다.");

        return "redirect:/notice/noticeAdminList?currentPage=" + currentPage
                + "&field=" + enc(field)
                + "&keyword=" + enc(keyword);
    }

    /* =========================
       7) 공지 삭제 (soft delete)
       ========================= */
    @PostMapping("/removeNotice")
    public String removeNotice(@RequestParam int noticeNum,
                               @RequestParam(defaultValue = "1") int currentPage,
                               @RequestParam(defaultValue = "1") String field,
                               @RequestParam(defaultValue = "") String keyword,
                               RedirectAttributes ra,
                               HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        int cnt = nas.removeAdminNotice(noticeNum);

        ra.addFlashAttribute("msg",
                cnt == 1 ? "공지사항이 삭제되었습니다." : "공지사항 삭제에 실패했습니다.");

        return "redirect:/notice/noticeAdminList?currentPage=" + currentPage
                + "&field=" + enc(field)
                + "&keyword=" + enc(keyword);
    }
}