package kr.co.noir.notice;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class NoticeAdminService {

	@Qualifier("noticeAdminDAO")
	@Autowired
	private NoticeAdminDAO naDAO;


    /* ===================== 페이징 관련 ===================== */

    /** 한 페이지에 보여줄 글 수 */
    public int pageScale() {
        return 10;
    }

    /** 전체 페이지 수 */
    public int totalPage(int totalCnt, int pageScale) {
        return (int) Math.ceil((double) totalCnt / pageScale);
    }

    /** 시작 번호 */
    public int startNum(int currentPage, int pageScale) {
        return currentPage * pageScale - pageScale + 1;
    }

    /** 끝 번호 */
    public int endNum(int startNum, int pageScale) {
        return startNum + pageScale - 1;
    }

    /* ===================== 공지사항 ===================== */

    /** 관리자 공지사항 총 개수 */
	public int totalCnt(BoardRangeDTO rDTO) {
		int totalCnt = 0;
		try {
			setCategory(rDTO);
			totalCnt = naDAO.selectNoticeTotal(rDTO);

		} catch (SQLException se) {
			se.printStackTrace();

		} // end catch

		return totalCnt;
	}// totalCnt
    

    /** 관리자 공지사항 목록 */
    public List<NoticeAdminDomain> searchAdminNoticeList(BoardRangeDTO rDTO) {
        List<NoticeAdminDomain> list = null;
        try {
        	setCategory(rDTO);
            list = naDAO.selectNoticeList(rDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    
    //카테고리 
    public void setCategory(BoardRangeDTO rDTO) {

        String field = rDTO.getField();
        String keyword = rDTO.getKeyword();

        // 1) keyword가 ""(빈값/공백)이면 null로 정리 (XML if 조건 안정화)
        if (keyword != null && keyword.trim().isEmpty()) {
            rDTO.setKeyword(null);
        }

        // 2) 카테고리 선택(field 2~4)이면 category 세팅 + keyword는 끈다
        if ("2".equals(field)) {
            rDTO.setCategory("일반");
            rDTO.setKeyword(null);
            return;
        }
        if ("3".equals(field)) {
            rDTO.setCategory("멤버십");
            rDTO.setKeyword(null);
            return;
        }
        if ("4".equals(field)) {
            rDTO.setCategory("약관개정");
            rDTO.setKeyword(null);
            return;
        }

        // 3) 그 외(field 1/null/기타)는 카테고리 조건 없음(전체/제목검색)
        rDTO.setCategory(null);
    }


    
	 //제목이 20자를 초과하면 19자까지 보여주고 뒤에 ..을 붙이는일
	public void titleSubStr(List<NoticeAdminDomain> boardlist) {
		String title = "";
		for (NoticeAdminDomain bDTO : boardlist) {
			title = bDTO.getNoticeTitle();
			if (title.length() > 19) {
				bDTO.setNoticeTitle(title.substring(0, 20) + "...");

			} // end if
		} // end for
	}// titleSubStr
	
	
    /** 관리자 공지사항 상세 */
    public NoticeAdminDomain searchOneNotice(int noticeNum) {
        NoticeAdminDomain naDomain = null;
        try {
        	naDomain = naDAO.selectNoticeDetail(noticeNum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return naDomain;
    }

    /** 관리자 공지사항 등록 */
    public int addAdminNotice(NoticeAdminDTO naDTO) {
        int cnt = 0;
        try {
            naDAO.insertNotice(naDTO);
            cnt = 1;
        } catch (PersistenceException pe) {
            pe.printStackTrace();
        }
        return cnt;
    }

    //관리자 공지사항 수정
	public int modifyNotice(NoticeAdminDTO naDTO) {
		int cnt = 0;

		try {
			cnt = naDAO.updateNotice(naDTO);
		} catch (SQLException e) {
			e.printStackTrace();
		} // end catch

		return cnt;
	}// modifyBoard
    
    /** 관리자 공지사항 삭제 */
    public int removeAdminNotice(int noticeNum) {
        int cnt = 0;
        try {
            cnt = naDAO.deleteNotice(noticeNum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnt;
    }

    /* ===================== 페이지네이션 HTML ===================== */

    public String pagination(BoardRangeDTO rDTO) {

        StringBuilder sb = new StringBuilder();

        int pageBlock = 5;
        int startPage = ((rDTO.getCurrentPage() - 1) / pageBlock) * pageBlock + 1;
        int endPage = startPage + pageBlock - 1;

        if (endPage > rDTO.getTotalPage()) {
            endPage = rDTO.getTotalPage();
        }

        // ✅ 검색 파라미터 유지용 쿼리스트링
        String query = "";
        if (rDTO.getField() != null && !rDTO.getField().isBlank()) {
            query += "&field=" + rDTO.getField();
        }
        if (rDTO.getKeyword() != null && !rDTO.getKeyword().isBlank()) {
            // 공백 등 깨질 수 있어서 인코딩이 정석이지만,
            // 지금은 최소 수정으로 유지 (가능하면 URLEncoder 권장)
            query += "&keyword=" + rDTO.getKeyword();
        }

        sb.append("<ul class='pagination'>");

        // 이전
        if (startPage > 1) {
            sb.append("<li><a href='")
              .append(rDTO.getUrl())
              .append("?currentPage=").append(startPage - 1)
              .append(query)
              .append("'>이전</a></li>");
        }

        // 페이지 번호
        for (int i = startPage; i <= endPage; i++) {
            if (i == rDTO.getCurrentPage()) {
                sb.append("<li class='active'><span>")
                  .append(i).append("</span></li>");
            } else {
                sb.append("<li><a href='")
                  .append(rDTO.getUrl())
                  .append("?currentPage=").append(i)
                  .append(query)
                  .append("'>").append(i).append("</a></li>");
            }
        }

        // 다음
        if (endPage < rDTO.getTotalPage()) {
            sb.append("<li><a href='")
              .append(rDTO.getUrl())
              .append("?currentPage=").append(endPage + 1)
              .append(query)
              .append("'>다음</a></li>");
        }

        sb.append("</ul>");

        return sb.toString();
    }
}
