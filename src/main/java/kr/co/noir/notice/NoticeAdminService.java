package kr.co.noir.notice;

import java.sql.SQLException;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeAdminService {

    @Autowired
    private NoticeAdminDAO naDAO;

    /** 총 게시물 수 */
    public int totalCnt(BoardRangeDTO rDTO) {
        int totalCnt = 0;
        try {
            totalCnt = naDAO.selectNoticeTotalCnt(rDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCnt;
    }

    /** 한 화면에 보여줄 게시물 수 */
    public int pageScale() {
        return 10;
    }

    /** 총 페이지 수 */
    public int totalPage(int totalCount, int pageScale) {
        return (int) Math.ceil((double) totalCount / pageScale);
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
        List<NoticeAdminDomain> list = null;
        try {
            list = naDAO.selectNoticeList(rDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** (선택) 제목이 길면 ... 처리 */
    public void titleSubStr(List<NoticeAdminDomain> noticeList) {
        if (noticeList == null) return;

        for (NoticeAdminDomain n : noticeList) {
            String title = n.getNoticeTitle();
            if (title != null && title.length() > 20) {
                n.setNoticeTitle(title.substring(0, 20) + "...");
            }
        }
    }

    /**
     * 페이지네이션 (field + keyword 유지)
     * - rDTO.url 은 컨트롤러에서 "/notice/noticeAdminList" 형태로 세팅되어 있어야 함
     */


    public String pagination2(BoardRangeDTO rDTO, String justify) {
        StringBuilder pagination = new StringBuilder();

        int pageNumber = 3;
        int startPage = ((rDTO.getCurrentPage() - 1) / pageNumber) * pageNumber + 1;
        int endPage = startPage + pageNumber - 1;

        if (endPage > rDTO.getTotalPage()) endPage = rDTO.getTotalPage();

        // ✅ 파라미터(필터/검색) 유지용
        String field = (rDTO.getField() == null) ? "1" : rDTO.getField();
        String keyword = (rDTO.getKeyword() == null) ? "" : rDTO.getKeyword();
        String encKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        // URL 기본값 방어
        String baseUrl = rDTO.getUrl();
        if (baseUrl == null || baseUrl.isBlank()) baseUrl = "/notice/noticeAdminList";

        // justify 보정
        if (!("center".equals(justify) || "left".equals(justify) || "right".equals(justify))) {
            justify = "center";
        }

        // ✅ 쿼리 스트링 조립 (항상 field 유지, keyword는 빈값 허용)
        StringBuilder qs = new StringBuilder();
        qs.append("&field=").append(field);
        if (!keyword.isEmpty()) {
            qs.append("&keyword=").append(encKeyword);
        } else {
            // 빈 키워드도 유지하고 싶으면 아래 주석 해제
            // qs.append("&keyword=");
        }

        int movePage;

        // 이전
        StringBuilder prevMark = new StringBuilder();
        if (rDTO.getCurrentPage() > pageNumber) {
            movePage = startPage - 1;
            prevMark.append("<li class='page-item'><a class='page-link' href='")
                    .append(baseUrl)
                    .append("?currentPage=").append(movePage)
                    .append(qs)
                    .append("'>이전</a></li>");
        } else {
            prevMark.append("<li class='page-item disabled'><span class='page-link'>이전</span></li>");
        }

        // 페이지 번호
        StringBuilder pageLink = new StringBuilder();
        movePage = startPage;

        while (movePage <= endPage) {
            if (movePage == rDTO.getCurrentPage()) {
                pageLink.append("<li class='page-item active'><span class='page-link'>")
                        .append(movePage).append("</span></li>");
            } else {
                pageLink.append("<li class='page-item'><a class='page-link' href='")
                        .append(baseUrl)
                        .append("?currentPage=").append(movePage)
                        .append(qs)
                        .append("'>")
                        .append(movePage)
                        .append("</a></li>");
            }
            movePage++;
        }

        // 다음
        StringBuilder nextMark = new StringBuilder();
        if (rDTO.getTotalPage() > endPage) {
            movePage = endPage + 1;
            nextMark.append("<li class='page-item'><a class='page-link' href='")
                    .append(baseUrl)
                    .append("?currentPage=").append(movePage)
                    .append(qs)
                    .append("'>다음</a></li>");
        } else {
            nextMark.append("<li class='page-item disabled'><span class='page-link'>다음</span></li>");
        }

        pagination.append("<nav aria-label='pagination'>")
                .append("<ul class='pagination d-flex justify-content-").append(justify).append("'>")
                .append(prevMark).append(pageLink).append(nextMark)
                .append("</ul>")
                .append("</nav>");

        return pagination.toString();
    }

    /** 상세 */
    public NoticeAdminDomain searchOneNotice(int noticeNum) {
        NoticeAdminDomain naDomain = null;
        try {
            naDomain = naDAO.selectNoticeDetail(noticeNum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return naDomain;
    }

    /** 등록 */
    public int addAdminNotice(NoticeAdminDTO naDTO) {
        int cnt = 0;
        try {
            cnt = naDAO.insertNotice(naDTO); // ✅ 여기서 SQLException 발생 가능
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }

    /** 수정 */
    public int modifyNotice(NoticeAdminDTO naDTO) {
        int cnt = 0;
        try {
            cnt = naDAO.updateNotice(naDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }

    /** 삭제(soft delete) */
    public int removeAdminNotice(int noticeNum) {
        int cnt = 0;
        try {
            cnt = naDAO.deleteNotice(noticeNum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }
}