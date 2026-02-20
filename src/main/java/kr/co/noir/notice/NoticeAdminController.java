package kr.co.noir.notice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/notice")
public class NoticeAdminController {

    @Autowired
    private NoticeAdminService nas;

    private Integer getAdminNum(HttpSession session) {
        return (Integer) session.getAttribute("adminNum");
    }

    // 1) 공지사항 목록
    @GetMapping("/noticeAdminList")
    public String adminNoticeList(BoardRangeDTO rDTO, Model model, HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) {
            return "redirect:/admin/login";
        }

        int currentPage = rDTO.getCurrentPage();
        if (currentPage <= 0) currentPage = 1;
        rDTO.setCurrentPage(currentPage);

        if (rDTO.getField() == null || rDTO.getField().trim().isEmpty()) {
            rDTO.setField("1");
        }
        if (rDTO.getKeyword() == null) {
            rDTO.setKeyword("");
        }

        int totalCnt = nas.totalCnt(rDTO);
        int pageScale = nas.pageScale();
        int totalPage = nas.totalPage(totalCnt, pageScale);

        int startNum = nas.startNum(currentPage, pageScale);
        int endNum = nas.endNum(startNum, pageScale);

        rDTO.setStartNum(startNum);
        rDTO.setEndNum(endNum);
        rDTO.setTotalPage(totalPage);
        rDTO.setUrl("/notice/noticeAdminList");

        List<NoticeAdminDomain> noticeList = nas.searchAdminNoticeList(rDTO);
        String pagination = nas.pagination2(rDTO, "center");

        model.addAttribute("listNum", totalCnt - (currentPage - 1) * pageScale);
        model.addAttribute("noticeList", noticeList);
        model.addAttribute("pagination", pagination);
        model.addAttribute("rDTO", rDTO);

        return "manager/notice/noticeAdminList";
    }

    // 2) 공지 상세보기(선택)
    @GetMapping("/noticeDetailAdmin")
    public String noticeDetail(@RequestParam int noticeNum,
                               @RequestParam(defaultValue = "1") int currentPage,
                               Model model,
                               HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("notice", nas.searchOneNotice(noticeNum));
        model.addAttribute("currentPage", currentPage);

        return "manager/notice/noticeDetailAdmin";
    }

    // 3) 공지 작성 폼
    @GetMapping("/writeNoticeFrm")
    public String writeNoticeForm(@RequestParam(defaultValue = "1") int currentPage,
                                  Model model,
                                  HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("currentPage", currentPage);
        return "manager/notice/noticeWriteFrmAdmin";
    }

    // 4) 공지 작성 처리
    @PostMapping("/noticeWriteProcess")
    public String writeNoticeProcess(NoticeAdminDTO naDTO,
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

        return (cnt == 1)
                ? "redirect:/notice/noticeAdminList?currentPage=1"
                : "redirect:/notice/writeNoticeFrm?currentPage=1";
    }

    // 5) 공지 수정 폼 (작성 폼 재사용)
    @GetMapping("/modifyNoticeFrm")
    public String modifyNoticeForm(@RequestParam int noticeNum,
                                   @RequestParam(defaultValue = "1") int currentPage,
                                   Model model,
                                   HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("notice", nas.searchOneNotice(noticeNum));
        model.addAttribute("currentPage", currentPage);

        return "manager/notice/noticeWriteFrmAdmin";
    }

    // 6) 공지 수정 처리
    @PostMapping("/modifyNoticeProcess")
    public String modifyNoticeProcess(NoticeAdminDTO naDTO,
                                      @RequestParam(defaultValue = "1") int currentPage,
                                      RedirectAttributes ra,
                                      HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) {
            return "redirect:/admin/login";
        }

        naDTO.setAdminNum(adminNum);

        int cnt = nas.modifyNotice(naDTO);

        ra.addFlashAttribute("msg",
                cnt == 1 ? "수정되었습니다." : "수정에 실패했습니다.");

        return "redirect:/notice/noticeAdminList?currentPage=" + currentPage;
    }

    // 7) 공지 삭제 (soft delete)
    @PostMapping("/removeNotice")
    public String removeNotice(@RequestParam int noticeNum,
                               @RequestParam(defaultValue = "1") int currentPage,
                               RedirectAttributes ra,
                               HttpSession session) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) {
            return "redirect:/admin/login";
        }

        int cnt = nas.removeAdminNotice(noticeNum);

        ra.addFlashAttribute("msg",
                cnt == 1 ? "공지사항이 삭제되었습니다." : "공지사항 삭제에 실패했습니다.");

        return "redirect:/notice/noticeAdminList?currentPage=" + currentPage;
    }
}