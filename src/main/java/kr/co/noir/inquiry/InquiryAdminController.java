package kr.co.noir.inquiry;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/inquiryAdmin")
public class InquiryAdminController {

    @Autowired
    private InquiryAdminService ias;

    /**
     * 상세는 AJAX로만 조회
     */
    @ResponseBody
    @GetMapping("/inquiryDetailAjax")
    public InquiryAdminDomain inquiryDetailAjax(@RequestParam int inquiryNum) {
        return ias.searchInquiryDetail(inquiryNum);
    }

    /**
     * 문의사항 관리(상단 리스트)
     * - 하단 상세는 AJAX가 채움
     */
    @GetMapping("/inquiryListAdmin")
    public String inquiryListAdmin(InquiryRangeDTO irDTO,
                                   Model model,
                                   @RequestParam(required = false) Integer inquiryNum) {

        // 1) currentPage 안전 처리
        int currentPage = irDTO.getCurrentPage();
        if (currentPage <= 0) {
            currentPage = 1;
            irDTO.setCurrentPage(currentPage);
        }

        // 2) 검색 기본값 (mapper field가 1/2/3 기대)
        if (irDTO.getField() == null || irDTO.getField().trim().isEmpty()) {
            irDTO.setField("1"); // 기본: 제목
        }
        if (irDTO.getKeyword() == null) {
            irDTO.setKeyword("");
        }

        // 3) 페이징 계산
        int totalCnt = ias.totalCnt(irDTO);
        int pageScale = ias.pageScale();
        int totalPage = ias.totalPage(totalCnt, pageScale);

        int startNum = ias.startNum(pageScale, currentPage);
        int endNum = ias.endNum(startNum, pageScale);

        irDTO.setStartNum(startNum);
        irDTO.setEndNum(endNum);
        irDTO.setTotalPage(totalPage);
        irDTO.setUrl("/inquiryAdmin/inquiryListAdmin");

        // 4) 목록 조회
        List<InquiryAdminDomain> inquiryList = ias.searchInquiryList(irDTO);
        String pagination = ias.pagination2(irDTO, "center");

        int listNum = totalCnt - (currentPage - 1) * pageScale;

        model.addAttribute("listNum", listNum);
        model.addAttribute("inquiryList", inquiryList);
        model.addAttribute("pagination", pagination);

        // 선택된 번호(있으면 active 처리용)
        model.addAttribute("selectedInquiryNum", inquiryNum);

        return "manager/inquiry/inquiryAdminList";
    }

    
    //답변 처리
    @PostMapping("/replyInquiryProcess")
    public String replyInquiryProcess(InquiryAdminDTO iaDTO,
                                      InquiryRangeDTO irDTO,
                                      HttpSession session,
                                      RedirectAttributes ra) {

        // ✅ 1) adminId를 세션에서 가져온다 (로그인 코드는 못 건드리니까 이걸 신뢰)
        String adminId = (String) session.getAttribute("adminId");
        System.out.println("session adminId = " + adminId);
        System.out.println("session adminNum = " + session.getAttribute("adminNum")); // 참고용

        if (adminId == null || adminId.trim().isEmpty()) {
            return "redirect:/admin/login";
        }

        // ✅ 2) adminId로 DB에서 진짜 admin_num(PK) 조회
        Integer realAdminNum = ias.selectAdminNumByAdminId(adminId);
        System.out.println("realAdminNum(DB) = " + realAdminNum);

        if (realAdminNum == null) {
            ra.addFlashAttribute("msg", "관리자 정보 오류. 다시 로그인 해주세요.");
            return "redirect:/admin/login";
        }

        // ✅ 3) FK가 만족되는 adminNum 세팅
        iaDTO.setAdminNum(realAdminNum);

        // ✅ 4) 답변 업데이트
        boolean flag = ias.updateInquiryReturn(iaDTO);
        ra.addFlashAttribute("msg", flag ? "답변이 등록되었습니다." : "답변 등록에 실패했습니다.");

        // ✅ 5) redirect (기존 그대로)
        StringBuilder redirect = new StringBuilder();
        redirect.append("redirect:/inquiryAdmin/inquiryListAdmin?currentPage=")
                .append(irDTO.getCurrentPage() <= 0 ? 1 : irDTO.getCurrentPage());

        redirect.append("&inquiryNum=").append(iaDTO.getInquiryNum());

        if (irDTO.getKeyword() != null && !irDTO.getKeyword().isEmpty()) {
            redirect.append("&field=").append(irDTO.getField())
                    .append("&keyword=").append(URLEncoder.encode(irDTO.getKeyword(), StandardCharsets.UTF_8));
        }

        return redirect.toString();
    }

    /**
     * 삭제 처리(논리삭제) - AJAX 안 씀
     * - 성공/실패 flash msg로 알림
     */
    @PostMapping("/removeInquiry")
    public String removeInquiry(@RequestParam int inquiryNum,
                                InquiryRangeDTO irDTO,
                                RedirectAttributes ra) {

        boolean flag = ias.removeInquiry(inquiryNum);

        ra.addFlashAttribute("msg", flag ? "삭제되었습니다." : "삭제에 실패했습니다.");

        StringBuilder redirect = new StringBuilder();
        redirect.append("redirect:/inquiryAdmin/inquiryListAdmin?currentPage=")
                .append(irDTO.getCurrentPage() <= 0 ? 1 : irDTO.getCurrentPage());

        if (irDTO.getKeyword() != null && !irDTO.getKeyword().isEmpty()) {
            redirect.append("&field=").append(irDTO.getField())
                    .append("&keyword=").append(URLEncoder.encode(irDTO.getKeyword(), StandardCharsets.UTF_8));
        }

        return redirect.toString();
    }

}