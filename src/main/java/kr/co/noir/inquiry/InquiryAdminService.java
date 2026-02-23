package kr.co.noir.inquiry;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InquiryAdminService {

    @Autowired
    private InquiryAdminDAO iaDAO;

    // 전체 문의 개수
    public int totalCnt(InquiryRangeDTO irDTO) {
        int totalCnt = 0;
        try {
            totalCnt = iaDAO.selectInquiryTotal(irDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCnt;
    }

    // 한 화면에 보여줄 게시물 수
    public int pageScale() {
        return 10;
    }

    // 총 페이지 수
    public int totalPage(int totalCount, int pageScale) {
        return (int) Math.ceil((double) totalCount / pageScale);
    }

    // 시작 번호
    public int startNum(int pageScale, int currentPage) {
        return pageScale * (currentPage - 1) + 1;
    }

    // 끝 번호
    public int endNum(int startNum, int pageScale) {
        return startNum + pageScale - 1;
    }

    /**
     * 제목이 20자를 초과하면 20자까지 보여주고 ...을 붙이는 일
     */
    public void titleSubStr(List<InquiryAdminDomain> iaList) {
        if (iaList == null) return;

        String title = "";
        for (InquiryAdminDomain d : iaList) {
            title = d.getInquiryTitle();
            if (title != null && title.length() > 19) {
                d.setInquiryTitle(title.substring(0, 20) + "...");
            }
        }
    }

    // 문의 목록 조회
    public List<InquiryAdminDomain> searchInquiryList(InquiryRangeDTO irDTO) {
        List<InquiryAdminDomain> list = null;
        try {
            list = iaDAO.selectInquiryList(irDTO);
            titleSubStr(list);

            // ✅ 표시용 필드 세팅 (목록용)
            applyViewFieldsToList(list);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 문의 상세 조회
    public InquiryAdminDomain searchInquiryDetail(int inquiryNum) {
        InquiryAdminDomain detail = null;
        try {
            detail = iaDAO.selectInquiryDetail(inquiryNum);

            // ✅ 표시용 필드 세팅 (상세용)
            applyViewFieldsToOne(detail);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detail;
    }

    // 답변 등록/수정
    // iaDTO에는 inquiryNum + inquiryReturn이 세팅되어 있어야 함
    public boolean updateInquiryReturn(InquiryAdminDTO iaDTO) {
        boolean flag = false;
        try {
            flag = iaDAO.updateInquiry(iaDTO) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return flag;
    }

    // 삭제(논리삭제)
    public boolean removeInquiry(int inquiryNum) {
        boolean flag = false;
        try {
            flag = iaDAO.deleteInquiry(inquiryNum) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    // adminId로 admin_num(PK) 조회
    public Integer selectAdminNumByAdminId(String adminId) {
        Integer adminNum = null;
        try {
            adminNum = iaDAO.selectAdminNumByAdminId(adminId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminNum;
    }

    public String pagination2(InquiryRangeDTO rDTO, String justify) {
        StringBuilder pagination = new StringBuilder();

        int pageNumber = 3;
        int startPage = ((rDTO.getCurrentPage() - 1) / pageNumber) * pageNumber + 1;
        int endPage = (((startPage - 1) + pageNumber) / pageNumber) * pageNumber;

        if (rDTO.getTotalPage() <= endPage) {
            endPage = rDTO.getTotalPage();
        }

        boolean hasKeyword = (rDTO.getKeyword() != null && !rDTO.getKeyword().isEmpty());
        String encodedKeyword = hasKeyword
                ? URLEncoder.encode(rDTO.getKeyword(), StandardCharsets.UTF_8)
                : "";

        int movePage = 0;

        StringBuilder prevMark = new StringBuilder();
        prevMark.append("<li class='page-item disabled'>")
                .append("<a class='page-link'>Previous</a>")
                .append("</li>");

        if (rDTO.getCurrentPage() > pageNumber) {
            movePage = startPage - 1;
            prevMark.setLength(0);
            prevMark.append("<li class='page-item'><a class='page-link' href='")
                    .append(rDTO.getUrl())
                    .append("?currentPage=").append(movePage);

            if (hasKeyword) {
                prevMark.append("&field=").append(rDTO.getField())
                        .append("&keyword=").append(encodedKeyword);
            }

            prevMark.append("'>Previous</a></li>");
        }

        StringBuilder pageLink = new StringBuilder();
        movePage = startPage;

        while (movePage <= endPage) {
            if (movePage == rDTO.getCurrentPage()) {
                pageLink.append("<li class='page-item active'>")
                        .append("<a class='page-link'>").append(movePage).append("</a>")
                        .append("</li>");
            } else {
                pageLink.append("<li class='page-item'><a class='page-link' href='")
                        .append(rDTO.getUrl())
                        .append("?currentPage=").append(movePage);

                if (hasKeyword) {
                    pageLink.append("&field=").append(rDTO.getField())
                            .append("&keyword=").append(encodedKeyword);
                }

                pageLink.append("'>").append(movePage).append("</a></li>");
            }
            movePage++;
        }

        StringBuilder nextMark = new StringBuilder("<li class='page-item disabled'><a class='page-link'>Next</a></li>");
        if (rDTO.getTotalPage() > endPage) {
            movePage = endPage + 1;
            nextMark.setLength(0);
            nextMark.append("<li class='page-item'><a class='page-link' href='")
                    .append(rDTO.getUrl())
                    .append("?currentPage=").append(movePage);

            if (hasKeyword) {
                nextMark.append("&field=").append(rDTO.getField())
                        .append("&keyword=").append(encodedKeyword);
            }

            nextMark.append("'>Next</a></li>");
        }

        if (!("center".equals(justify) || "left".equals(justify))) {
            justify = "left";
        }

        pagination.append("<nav aria-label='...'>")
                .append("<ul class='pagination d-flex justify-content-")
                .append(justify)
                .append("'>")
                .append(prevMark).append(pageLink).append(nextMark)
                .append("</ul>")
                .append("</nav>");

        return pagination.toString();
    }

    /* =========================
       ✅ 표시용 필드 세팅 (복호화)
       - emailView : memberEmail 복호화
       - writerView: 기본은 emailView로 세팅(작성자 칸에 이메일을 보여주고 싶을 때)
         * 작성자 칸에 memberId를 보여주고 싶다면 아래 한 줄만 바꾸면 됨.
       ========================= */

    private void applyViewFieldsToList(List<InquiryAdminDomain> list) {
        if (list == null) return;
        for (InquiryAdminDomain d : list) {
            applyViewFieldsToOne(d);
        }
    }

    private void applyViewFieldsToOne(InquiryAdminDomain d) {
        if (d == null) return;

        // ✅ 이메일(암호화 저장) -> 복호화 표시
        String emailView = decryptSafe(d.getMemberEmail());
        d.setEmailView(emailView);

        // ✅ 작성자 칸에는 아이디 표시 (아이디는 평문 저장 가정)
        if (d.getMemberId() != null && !d.getMemberId().isBlank()) {
            d.setWriterView(d.getMemberId());
        } else {
            d.setWriterView("-");
        }
    }

    private String decryptSafe(String enc) {
        if (enc == null || enc.isBlank()) return "-";
        try {
            return CryptoUtil.decrypt(enc); // 너희 프로젝트 복호화 유틸로 교체
        } catch (Exception e) {
            return "-";
        }
    }

} // class