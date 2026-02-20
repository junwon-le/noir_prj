package kr.co.noir.event;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/event")
@Controller
public class EventAdminController {

    @Autowired
    private EventAdminService eas;

    // 세션 체크
    private Integer getAdminNum(HttpSession session) {
        return (Integer) session.getAttribute("adminNum");
    }

    // ✅ 임시 업로드 경로 (나중에 확정되면 여기만 바꾸면 됨)
    private static final String UPLOAD_DIR = "C:/temp/event/";

    /**
     * 이벤트 목록
     * GET /event/eventAdminList
     */
    @GetMapping("/eventAdminList")
    public String eventAdminList(EventRangeDTO erDTO, Model model, HttpSession session) {
        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        int currentPage = erDTO.getCurrentPage();
        if (currentPage <= 0) currentPage = 1;
        erDTO.setCurrentPage(currentPage);

        int totalCnt = eas.totalCnt(erDTO);
        int pageScale = eas.pageScale();
        int totalPage = eas.totalPage(totalCnt, pageScale);

        int startNum = eas.startNum(currentPage, pageScale);
        int endNum = eas.endNum(startNum, pageScale);

        erDTO.setStartNum(startNum);
        erDTO.setEndNum(endNum);
        erDTO.setTotalPage(totalPage);
        erDTO.setUrl("/event/eventAdminList");

        List<EventAdminDomain> list = eas.searchEventList(erDTO);
        String pagination = eas.pagination(erDTO);
        int listNum = totalCnt - (currentPage - 1) * pageScale;

        model.addAttribute("listNum", listNum);
        model.addAttribute("eventList", list);
        model.addAttribute("pagination", pagination);
        model.addAttribute("range", erDTO);

        return "/manager/event/eventAdminList";
    }

    /**
     * 이벤트 상세
     * GET /event/eventAdminDetail?eventNum=1
     */
    @GetMapping("/eventAdminDetail")
    public String eventAdminDetail(@RequestParam int eventNum, Model model, HttpSession session) {
        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        EventAdminDomain eaDomain = eas.searchOneEvent(eventNum);
        model.addAttribute("eaDomain", eaDomain);

        return "/manager/event/eventAdminDetail";
    }

    /**
     * 이벤트 등록 폼
     * GET /event/addAdminEventFrm
     * 템플릿: /templates/manager/event/eventAdminForm.html
     */
    @GetMapping("/addAdminEventFrm")
    public String addAdminEventForm(HttpSession session, Model model) {
        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        model.addAttribute("adminNum", adminNum);
        model.addAttribute("mode", "add");
        return "/manager/event/eventAdminForm";
    }

    /**
     * 이벤트 등록 처리 (이미지 업로드 포함)
     * POST /event/addAdminEventProcess
     */
    @PostMapping("/addAdminEventProcess")
    public String addAdminEventProcess(
            EventAdminDTO eaDTO,
            @RequestParam(required = false) MultipartFile img1,
            @RequestParam(required = false) MultipartFile img2,
            @RequestParam(defaultValue = "1") int currentPage,
            HttpSession session,
            Model model) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        eaDTO.setAdminNum(adminNum);

        new File(UPLOAD_DIR).mkdirs();

        // img1 저장
        if (img1 != null && !img1.isEmpty()) {
            String saved1 = saveFileNoThrow(UPLOAD_DIR, img1);
            if (saved1 == null) {
                model.addAttribute("flag", false);
                model.addAttribute("msg", "이미지1 업로드 실패");
                model.addAttribute("redirectUrl", "/event/eventAdminList?currentPage=" + currentPage);
                return "/manager/event/eventResult";
            }
            eaDTO.setEventImg1(saved1);
        }

        // img2 저장
        if (img2 != null && !img2.isEmpty()) {
            String saved2 = saveFileNoThrow(UPLOAD_DIR, img2);
            if (saved2 == null) {
                model.addAttribute("flag", false);
                model.addAttribute("msg", "이미지2 업로드 실패");
                model.addAttribute("redirectUrl", "/event/eventAdminList?currentPage=" + currentPage);
                return "/manager/event/eventResult";
            }
            eaDTO.setEventImg2(saved2);
        }
        
        //DB에 저장
        boolean flag = eas.addEvent(eaDTO);

        model.addAttribute("flag", flag);
        model.addAttribute("msg", flag ? "이벤트 추가 완료" : "이벤트 추가 실패");
        model.addAttribute("redirectUrl", "/event/eventAdminList?currentPage=" + currentPage);

        return "/manager/event/eventResult";
    }


    /**
     * 이벤트 수정 폼 (등록 폼 재사용)
     * GET /event/modifyAdminEventFrm?eventNum=1
     */
    @GetMapping("/modifyAdminEventFrm")
    public String modifyAdminEventFrm(@RequestParam int eventNum, HttpSession session, Model model) {
        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        EventAdminDomain eaDomain = eas.searchOneEvent(eventNum);
        model.addAttribute("eaDomain", eaDomain);
        model.addAttribute("mode", "edit");

        return "/manager/event/eventAdminForm";
    }

    /**
     * 이벤트 수정 처리 (이미지 업로드 포함)
     * POST /event/modifyEventProcess
     */
    @PostMapping("/modifyEventProcess")
    public String modifyEventProcess(
            EventAdminDTO eaDTO,
            @RequestParam(required = false) MultipartFile img1,
            @RequestParam(required = false) MultipartFile img2,
            @RequestParam(defaultValue = "1") int currentPage,
            HttpSession session,
            Model model) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        eaDTO.setAdminNum(adminNum);

        new File(UPLOAD_DIR).mkdirs();

        if (img1 != null && !img1.isEmpty()) {
            String saved1 = saveFileNoThrow(UPLOAD_DIR, img1);
            if (saved1 == null) {
                model.addAttribute("flag", false);
                model.addAttribute("msg", "이미지1 업로드 실패");
                model.addAttribute("redirectUrl", "/event/eventAdminList?currentPage=" + currentPage);
                return "/manager/event/eventResult";
            }
            eaDTO.setEventImg1(saved1);
        }

        if (img2 != null && !img2.isEmpty()) {
            String saved2 = saveFileNoThrow(UPLOAD_DIR, img2);
            if (saved2 == null) {
                model.addAttribute("flag", false);
                model.addAttribute("msg", "이미지2 업로드 실패");
                model.addAttribute("redirectUrl", "/event/eventAdminList?currentPage=" + currentPage);
                return "/manager/event/eventResult";
            }
            eaDTO.setEventImg2(saved2);
        }

        boolean flag = eas.modifyEvent(eaDTO);

        model.addAttribute("flag", flag);
        model.addAttribute("msg", flag ? "이벤트 수정 성공" : "이벤트 수정 실패");
        model.addAttribute("redirectUrl", "/event/eventAdminList?currentPage=" + currentPage);

        return "/manager/event/eventResult";
    }

    /**
     * 이벤트 삭제 처리
     * POST /event/removeEventProcess
     */
    @PostMapping("/removeEventProcess")
    public String removeEvent(@RequestParam int eventNum,
                              @RequestParam(defaultValue = "1") int currentPage,
                              HttpSession session,
                              Model model) {

        Integer adminNum = getAdminNum(session);
        if (adminNum == null) return "redirect:/admin/login";

        EventAdminDTO dto = new EventAdminDTO();
        dto.setEventNum(eventNum);
        dto.setAdminNum(adminNum);

        boolean flag = eas.removeEvent(dto);

        model.addAttribute("flag", flag);
        model.addAttribute("msg", flag ? "이벤트 삭제 성공" : "이벤트 삭제 실패");
        model.addAttribute("redirectUrl", "/event/eventAdminList?currentPage=" + currentPage);

        return "/manager/event/eventResult";
    }

    // =========================
    // 파일 저장 유틸 (예외를 밖으로 던지지 않음)
    // 실패하면 null 반환
    // =========================
    private String saveFileNoThrow(String uploadDir, MultipartFile file) {
        try {
            String original = file.getOriginalFilename();
            String ext = "";

            if (original != null && original.lastIndexOf(".") != -1) {
                ext = original.substring(original.lastIndexOf("."));
            }

            String savedName = UUID.randomUUID().toString().replace("-", "") + ext;
            File dest = new File(uploadDir, savedName);
            file.transferTo(dest);

            return savedName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}