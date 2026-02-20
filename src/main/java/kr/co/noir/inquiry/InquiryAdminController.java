package kr.co.noir.inquiry;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/inquiryAdmin")
public class InquiryAdminController {
	
	@Autowired
	private InquiryAdminService ias;
	
	@ResponseBody
	@GetMapping("/inquiryDetailAjax")
	public InquiryAdminDomain inquiryDetailAjax(@RequestParam int inquiryNum) {
	    return ias.searchInquiryDetail(inquiryNum);
	}

    /**
     * 문의사항 관리(상단 리스트 + 하단 상세)
     * - inquiryNum이 있으면 하단 상세 채움
     */
	@GetMapping("/inquiryListAdmin")
	public String searchInquiryList( Model model, 
									@RequestParam(required = false) Integer inquiryNum, 
									 InquiryRangeDTO irDTO ) {

        int totalCnt = ias.totalCnt(irDTO);
        int pageScale = ias.pageScale();
        int totalPage = ias.totalPage(totalCnt, pageScale);

        int currentPage = irDTO.getCurrentPage();
        if (currentPage <= 0) { // 안전
            currentPage = 1;
            irDTO.setCurrentPage(currentPage);
        }

        int startNum = ias.startNum(pageScale, currentPage);
        int endNum = ias.endNum(startNum, pageScale);

        irDTO.setCurrentPage(currentPage);
        irDTO.setEndNum(endNum);
        irDTO.setStartNum(startNum);
        irDTO.setTotalPage(totalPage);
        irDTO.setUrl("/inquiryAdmin/inquiryListAdmin");

        List<InquiryAdminDomain> inquiryList = ias.searchInquiryList(irDTO);
        String pagination = ias.pagination2(irDTO, "center");

        model.addAttribute("listNum", (totalCnt - (currentPage - 1) * pageScale));
        model.addAttribute("inquiryList", inquiryList);
        model.addAttribute("pagination", pagination);

        // ✅ 하단 상세
        InquiryAdminDomain detail = null;

        // inquiryNum이 없으면: 첫 번째 글 자동 선택(원하면 이 부분 제거 가능)
        if (inquiryNum == null && inquiryList != null && !inquiryList.isEmpty()) {
            inquiryNum = inquiryList.get(0).getInquiryNum();
        }

        if (inquiryNum != null) {
            detail = ias.searchInquiryDetail(inquiryNum);
        }

        model.addAttribute("detail", detail);
        model.addAttribute("selectedInquiryNum", inquiryNum);

        return "manager/inquiry/inquiryAdminList";
	}

    // 문의 답변 처리 (POST 권장)
    @PostMapping("/replyInquiryProcess")
    public String replyInquiryProcess(InquiryAdminDTO iaDTO, InquiryRangeDTO irDTO) {

        ias.updateInquiryReturn(iaDTO);

        // 상세 유지 + 검색 유지
        StringBuilder redirect = new StringBuilder();
        redirect.append("redirect:/inquiryAdmin/inquiryListAdmin?currentPage=")
                .append(irDTO.getCurrentPage() <= 0 ? 1 : irDTO.getCurrentPage())
                .append("&inquiryNum=").append(iaDTO.getInquiryNum());

        if (irDTO.getKeyword() != null && !irDTO.getKeyword().isEmpty()) {
            redirect.append("&field=").append(irDTO.getField())
                    .append("&keyword=").append(irDTO.getKeyword());
        }

        return redirect.toString();
    }

	

    // 문의 삭제(논리삭제)
    @PostMapping("/removeInquiry")
    public String removeInquiry(@RequestParam int inquiryNum, InquiryRangeDTO irDTO) {

        ias.removeInquiry(inquiryNum);

        StringBuilder redirect = new StringBuilder();
        redirect.append("redirect:/inquiryAdmin/inquiryListAdmin?currentPage=")
                .append(irDTO.getCurrentPage() <= 0 ? 1 : irDTO.getCurrentPage());

        if (irDTO.getKeyword() != null && !irDTO.getKeyword().isEmpty()) {
            redirect.append("&field=").append(irDTO.getField())
                    .append("&keyword=").append(irDTO.getKeyword());
        }

        return redirect.toString();
    }
	
}//class
