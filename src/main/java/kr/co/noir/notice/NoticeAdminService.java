package kr.co.noir.notice;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeAdminService {

    @Autowired
    private NoticeAdminDAO naDAO;

    /** 총 게시물 수 */
    public int totalCnt(BoardRangeDTO rDTO) {
        try {
            return naDAO.selectNoticeTotalCnt(rDTO);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 한 화면에 보여줄 게시물 수 */
    public int pageScale() {
        return 10;
    }

    /** 총 페이지 수 (✅ 최소 1 보장) */
    public int totalPage(int totalCount, int pageScale) {
        int tp = (int) Math.ceil((double) totalCount / pageScale);
        return (tp <= 0) ? 1 : tp;
    }

    /** 시작번호 */
    public int startNum(int currentPage, int pageScale) {
        return currentPage * pageScale - pageScale + 1;
    }

    /** 끝번호 */
    public int endNum(int startNum, int pageScale) {
        return startNum + pageScale - 1;
    }

    /** 목록 조회 */
    public List<NoticeAdminDomain> searchAdminNoticeList(BoardRangeDTO rDTO) {
        try {
            return naDAO.selectNoticeList(rDTO);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** (선택) 제목이 길면 ... 처리 */
    public void titleSubStr(List<NoticeAdminDomain> noticeList) {
        if (noticeList == null) return;

        for (NoticeAdminDomain n : noticeList) {
            if (n == null) continue;
            String title = n.getNoticeTitle();
            if (title != null && title.length() > 20) {
                n.setNoticeTitle(title.substring(0, 20) + "...");
            }
        }
    }

    /**
     * ✅ 버튼형 페이지네이션 (field + keyword 유지)
     * - rDTO.url 은 컨트롤러에서 "/notice/noticeAdminList" 형태로 세팅되어 있어야 함
     */
    public String pagination2(BoardRangeDTO rDTO, String justify) {

        // 방어
        if (rDTO == null) return "";

        StringBuilder sb = new StringBuilder();

        int pageNumber = 3;

        int currentPage = rDTO.getCurrentPage();
        if (currentPage <= 0) currentPage = 1;

        int totalPage = rDTO.getTotalPage();
        if (totalPage <= 0) totalPage = 1;

        int startPage = ((currentPage - 1) / pageNumber) * pageNumber + 1;
        int endPage = startPage + pageNumber - 1;
        if (endPage > totalPage) endPage = totalPage;

        // ✅ 파라미터 유지
        String field = (rDTO.getField() == null || rDTO.getField().trim().isEmpty()) ? "1" : rDTO.getField().trim();
        String keyword = (rDTO.getKeyword() == null) ? "" : rDTO.getKeyword().trim();
        String encKeyword = keyword.isEmpty() ? "" : URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        String baseUrl = rDTO.getUrl();
        if (baseUrl == null || baseUrl.isBlank()) baseUrl = "/notice/noticeAdminList";

        // justify 보정
        if (!("center".equals(justify) || "left".equals(justify) || "right".equals(justify))) {
            justify = "center";
        }

        // ✅ 공통 QS
        StringBuilder qs = new StringBuilder();
        qs.append("&field=").append(field);
        if (!keyword.isEmpty()) {
            qs.append("&keyword=").append(encKeyword);
        }

        // wrapper
        sb.append("<div style='text-align:").append(justify).append(";'>");

        // ===== 이전 =====
        if (currentPage > pageNumber) {
            int movePage = startPage - 1;
            sb.append("<a class='prevMark' href='")
              .append(baseUrl).append("?currentPage=").append(movePage)
              .append(qs)
              .append("'>&lt;&lt;</a>");
        } else {
            sb.append("<span class='currentPage' style='opacity:.35'>&lt;&lt;</span>");
        }

        // ===== 페이지 =====
        for (int p = startPage; p <= endPage; p++) {
            if (p == currentPage) {
                sb.append("<span class='currentPage'>").append(p).append("</span>");
            } else {
                sb.append("<a class='notCurrentPage' href='")
                  .append(baseUrl).append("?currentPage=").append(p)
                  .append(qs)
                  .append("'>").append(p).append("</a>");
            }
        }

        // ===== 다음 =====
        if (totalPage > endPage) {
            int movePage = endPage + 1;
            sb.append("<a class='nextMark' href='")
              .append(baseUrl).append("?currentPage=").append(movePage)
              .append(qs)
              .append("'>&gt;&gt;</a>");
        } else {
            sb.append("<span class='currentPage' style='opacity:.35'>&gt;&gt;</span>");
        }

        sb.append("</div>");
        return sb.toString();
    }

    /** 상세 */
    public NoticeAdminDomain searchOneNotice(int noticeNum) {
        try {
            return naDAO.selectNoticeDetail(noticeNum);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 등록 */
    public int addAdminNotice(NoticeAdminDTO naDTO) {
        try {
            return naDAO.insertNotice(naDTO);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 수정 */
    public int modifyNotice(NoticeAdminDTO naDTO) {
        try {
            return naDAO.updateNotice(naDTO);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 삭제(soft delete) */
    public int removeAdminNotice(int noticeNum) {
        try {
            return naDAO.deleteNotice(noticeNum);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /* =========================================================
       ✅ 추가: adminId로 adminNum 조회 (세션 보정용)
       - 컨트롤러에서 adminNum이 비었을 때 DB로 찾아 채우기 위함
       ========================================================= */
    public Integer findAdminNumByAdminId(String adminId) {
        if (adminId == null || adminId.trim().isEmpty()) return null;

        try {
            return naDAO.selectAdminNumByAdminId(adminId.trim());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}