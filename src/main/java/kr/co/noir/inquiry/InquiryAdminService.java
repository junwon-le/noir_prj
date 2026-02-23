package kr.co.noir.inquiry;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class InquiryAdminService {

    @Autowired
    private InquiryAdminDAO iaDAO;

    // ✅ InquiryService와 동일한 설정값 사용
    @Value("${user.crypto.key}")
    private String key;

    @Value("${user.crypto.salt}")
    private String salt;

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

    public int pageScale() { return 10; }

    public int totalPage(int totalCount, int pageScale) {
        return (int) Math.ceil((double) totalCount / pageScale);
    }

    public int startNum(int pageScale, int currentPage) {
        return pageScale * (currentPage - 1) + 1;
    }

    public int endNum(int startNum, int pageScale) {
        return startNum + pageScale - 1;
    }

    public void titleSubStr(List<InquiryAdminDomain> iaList) {
        if (iaList == null) return;

        for (InquiryAdminDomain d : iaList) {
            String title = d.getInquiryTitle();
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

            // ✅ 표시용 필드 세팅(목록)
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

            // ✅ 표시용 필드 세팅(상세)
            applyViewFieldsToOne(detail);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return detail;
    }

    // 답변 등록/수정
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
       ✅ 표시용 필드 세팅 (복호화 적용)
       - emailView : memberEmail 복호화
       - writerView: memberId 표시(현재 너 코드 기준 유지)
       ========================= */

    private void applyViewFieldsToList(List<InquiryAdminDomain> list) {
        if (list == null) return;
        for (InquiryAdminDomain d : list) {
            applyViewFieldsToOne(d);
        }
    }

    private void applyViewFieldsToOne(InquiryAdminDomain d) {
        if (d == null) return;

        // ✅ 이메일 복호화해서 표시용 필드에 세팅
        d.setEmailView(decryptEmailSafe(d.getMemberEmail()));

        // ✅ 작성자는 memberId 표시
        if (d.getMemberId() != null && !d.getMemberId().isBlank()) {
            d.setWriterView(d.getMemberId());
        } else {
            d.setWriterView("-");
        }
    }

    private String decryptEmailSafe(String encEmail) {
        if (encEmail == null || encEmail.isBlank()) return "-";
        try {
            TextEncryptor te = Encryptors.text(key, salt);
            return te.decrypt(encEmail);
        } catch (Exception e) {
            return "-";
        }
    }
}