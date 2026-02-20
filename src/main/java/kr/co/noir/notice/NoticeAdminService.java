package kr.co.noir.notice;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
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
        int endPage = (((startPage - 1) + pageNumber) / pageNumber) * pageNumber;

        if (rDTO.getTotalPage() <= endPage) {
            endPage = rDTO.getTotalPage();
        }

        int movePage = 0;

        // 이전
        StringBuilder prevMark = new StringBuilder();
        prevMark.append("<li class='page-item prev disabled'>")
                .append("<a class='page-link'>이전</a>")
                .append("</li>");

        if (rDTO.getCurrentPage() > pageNumber) {
            movePage = startPage - 1;
            prevMark.setLength(0);
            prevMark.append("<li class='page-item prev'><a class='page-link' href='")
                    .append(rDTO.getUrl())
                    .append("?currentPage=").append(movePage);

            // ✅ field/keyword 유지
            if (rDTO.getField() != null && !rDTO.getField().isEmpty()) {
                prevMark.append("&field=").append(rDTO.getField());
            }
            if (rDTO.getKeyword() != null && !rDTO.getKeyword().isEmpty()) {
                prevMark.append("&keyword=").append(rDTO.getKeyword());
            }

            prevMark.append("'>이전</a></li>");
        }

        // 페이지 링크
        StringBuilder pageLink = new StringBuilder();
        movePage = startPage;

        while (movePage <= endPage) {
            if (movePage == rDTO.getCurrentPage()) {
                pageLink.append("<li class='page-item active'>")
                        .append("<span class='page-link'>").append(movePage).append("</span>")
                        .append("</li>");
            } else {
                pageLink.append("<li class='page-item'><a class='page-link' href='")
                        .append(rDTO.getUrl())
                        .append("?currentPage=").append(movePage);

                // ✅ field/keyword 유지
                if (rDTO.getField() != null && !rDTO.getField().isEmpty()) {
                    pageLink.append("&field=").append(rDTO.getField());
                }
                if (rDTO.getKeyword() != null && !rDTO.getKeyword().isEmpty()) {
                    pageLink.append("&keyword=").append(rDTO.getKeyword());
                }

                pageLink.append("'>").append(movePage).append("</a></li>"); // ✅ 닫기까지!
            }
            movePage++;
        }

        // 다음
        StringBuilder nextMark = new StringBuilder(
                "<li class='page-item next disabled'><span class='page-link'>다음</span></li>"
        );

        if (rDTO.getTotalPage() > endPage) {
            movePage = endPage + 1;
            nextMark.setLength(0);
            nextMark.append("<li class='page-item next'><a class='page-link' href='")
                    .append(rDTO.getUrl())
                    .append("?currentPage=").append(movePage);

            // ✅ field/keyword 유지
            if (rDTO.getField() != null && !rDTO.getField().isEmpty()) {
                nextMark.append("&field=").append(rDTO.getField());
            }
            if (rDTO.getKeyword() != null && !rDTO.getKeyword().isEmpty()) {
                nextMark.append("&keyword=").append(rDTO.getKeyword());
            }

            nextMark.append("'>다음</a></li>");
        }

        if (!("center".equals(justify) || "left".equals(justify))) {
            justify = "left";
        }

        pagination.append("<nav aria-label='...'>")
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