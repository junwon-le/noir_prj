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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/notice")
public class NoticeAdminController {

    @Autowired
    private NoticeAdminService nas;
    
    //공지사항 목록
    @GetMapping("/noticeAdminList")
    public String adminNoticeList(BoardRangeDTO rDTO, Model model, HttpSession session) {

//        int currentPage = rDTO.getCurrentPage();
//        if (currentPage <= 0) {
//            currentPage = 1;
//            rDTO.setCurrentPage(currentPage);
//        }
//
//        // ✅ 추가: 기본값 세팅 (첫 화면에서 전체 목록 보장)
//        if (rDTO.getField() == null || rDTO.getField().trim().isEmpty()) {
//            rDTO.setField("1");
//        }
//        if (rDTO.getKeyword() == null) {
//            rDTO.setKeyword("");
//        }

        // 관리자 로그인 체크만
        String adminIp = (String) session.getAttribute("adminIp");
        if (adminIp == null) {
            return "redirect:/admin/login";
        }
	    
        int totalCnt = nas.totalCnt(rDTO);
        int pageScale = nas.pageScale();
        int totalPage = nas.totalPage(totalCnt, pageScale);
		int currentPage = rDTO.getCurrentPage(); //현재 페이지
        int startNum = nas.startNum(currentPage, pageScale);
        int endNum = nas.endNum(startNum, pageScale);

        rDTO.setStartNum(startNum);
        rDTO.setEndNum(endNum);
        rDTO.setTotalPage(totalPage);
        rDTO.setUrl("notice/noticeAdminList");

        List<NoticeAdminDomain> noticeList = nas.searchAdminNoticeList(rDTO);
        String pagination = nas.pagination2(rDTO,"center");//페이지 네이션


		model.addAttribute("listNum",totalCnt-(currentPage-1)*pageScale);
		model.addAttribute("noticeList", noticeList);
        model.addAttribute("pagination", pagination);

        return "manager/notice/noticeAdmin";
    }

    
    //공지사항 상세보기
    @GetMapping("/DetailNotice")
    public String NoticeDetail(@RequestParam int noticeNum, Model model) {
        model.addAttribute("notice", nas.searchOneNotice(noticeNum));
        return "manager/notice/noticeDetailAdmin";
    }
    
    //공지사항 작성 폼
    @GetMapping("/writeNoticeFrm")
    public String writeNoticeForm(HttpServletRequest request, Model model) {
        model.addAttribute("ip", request.getRemoteAddr());
        return "manager/notice/noticeWriteFrmAdmin";
    }
    //공지사항 작성 처리
    @PostMapping("/noticeWriteProcess")
    public String writeNoticeProcess(NoticeAdminDTO naDTO,
                                     HttpServletRequest request,
                                     HttpSession session,
                                     RedirectAttributes ra) {

        Integer adminNum = (Integer) session.getAttribute("adminNum");

        if (adminNum == null) {
            ra.addFlashAttribute("msg", "로그인이 필요합니다.");
            return "redirect:/noticeAdmin";  
        }

        naDTO.setAdminNum(adminNum);
        naDTO.setIp(request.getRemoteAddr());

        int cnt = nas.addAdminNotice(naDTO);

        ra.addFlashAttribute("msg",
                cnt == 1 ? "공지사항이 등록되었습니다." : "공지사항 등록에 실패했습니다.");

        return cnt == 1
                ? "redirect:/notice/searchAllNotice"
                : "redirect:/notice/writeNoticeFrm";
    }
    
    
 // 공지사항 수정
    @PostMapping("/modifyNoticeProcess")
    public String modifyNoticeProcess(NoticeAdminDTO naDTO,
                                      @RequestParam(defaultValue = "1") int currentPage,
                                      RedirectAttributes ra,
                                      HttpServletRequest request) {

        naDTO.setIp(request.getRemoteAddr());

        int cnt = nas.modifyNotice(naDTO); // 1 or 0

        ra.addFlashAttribute("msg",
                cnt == 1 ? "수정되었습니다." : "수정에 실패했습니다.");

        return "redirect:/notice/searchAllNotice?currentPage=" + currentPage;
    }


    
    
    //공지사항 삭제
	@PostMapping("/removeNotice")
	public String removeNotice(@RequestParam int noticeNum,
	                           @RequestParam(defaultValue = "1") int currentPage,
	                           Model model) {

	    int cnt = nas.removeAdminNotice(noticeNum);
	    boolean flag = (cnt == 1);

	    model.addAttribute("flag", flag);
	    model.addAttribute("currentPage", currentPage);

	    return "manager/notice/removeNoticeProcess"; 
	}

	}

