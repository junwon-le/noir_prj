package kr.co.noir.event;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class EventAdminService {

    @Autowired
    @Qualifier("eventAdminDAO") // DAO에 @Repository("eventAdminDAO") 있으니까 이거 유지 OK
    private EventAdminDAO eaDAO;

    public int totalCnt(EventRangeDTO erDTO) {
        int totalCnt = 0;
        try {
            totalCnt = eaDAO.selectEventTotalCnt(erDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCnt;
    }

    public int pageScale() {
        return 10;
    }

    // ✅ 수정 1) totalCnt=0이어도 totalPage 최소 1 보장
    public int totalPage(int totalCount, int pageScale) {
        int tp = (int) Math.ceil((double) totalCount / pageScale);
        return (tp <= 0) ? 1 : tp;
    }

    public int startNum(int currentPage, int pageScale) {
        return currentPage * pageScale - pageScale + 1;
    }

    public int endNum(int startNum, int pageScale) {
        return startNum + pageScale - 1;
    }

    public List<EventAdminDomain> searchEventList(EventRangeDTO erDTO) {
        List<EventAdminDomain> list = null;
        try {

            System.out.println(erDTO.getEndNum());
            System.out.println(erDTO.getStartNum());
            System.out.println(erDTO.getField());

            list = eaDAO.selectEventList(erDTO);
            System.out.println(list);

            if (list != null && !list.isEmpty()) {
                titleSubStr(list);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void titleSubStr(List<EventAdminDomain> eventList) {
        for (EventAdminDomain domain : eventList) {
            if (domain != null) {
                String title = domain.getEventTitle();
                if (title != null && title.length() > 20) {
                    domain.setEventTitle(title.substring(0, 20) + "...");
                }
            }
        }
    }

    public String pagination(EventRangeDTO erDTO) {
        StringBuilder pagiNation = new StringBuilder();

        int pageNumber = 3;
        int startPage = ((erDTO.getCurrentPage() - 1) / pageNumber) * pageNumber + 1;

        int endPage = startPage + pageNumber - 1;
        if (endPage > erDTO.getTotalPage()) {
            endPage = erDTO.getTotalPage();
        }

        String kw = erDTO.getKeyword();
        boolean hasKeyword = kw != null && !kw.trim().isEmpty();
        String safeKw = hasKeyword ? kw.trim() : "";

        int movePage = 0;

        // ✅ 이전
        if (erDTO.getCurrentPage() > pageNumber) {
            movePage = startPage - 1;
            pagiNation.append("<a class='prevMark' href='")
                      .append(erDTO.getUrl())
                      .append("?currentPage=")
                      .append(movePage);

            if (hasKeyword) {
                pagiNation.append("&keyword=").append(safeKw);
            }
            pagiNation.append("'>&lt;&lt;</a>");
        } else {
            // 비활성 느낌(원하면 CSS로 스타일)
            pagiNation.append("<span class='currentPage' style='opacity:.35'>&lt;&lt;</span>");
        }

        // ✅ 페이지 링크
        movePage = startPage;
        while (movePage <= endPage) {
            if (movePage == erDTO.getCurrentPage()) {
                pagiNation.append("<span class='currentPage'>")
                          .append(movePage)
                          .append("</span>");
            } else {
                pagiNation.append("<a class='notCurrentPage' href='")
                          .append(erDTO.getUrl())
                          .append("?currentPage=")
                          .append(movePage);

                if (hasKeyword) {
                    pagiNation.append("&keyword=").append(safeKw);
                }
                pagiNation.append("'>")
                          .append(movePage)
                          .append("</a>");
            }
            movePage++;
        }

        // ✅ 다음
        if (erDTO.getTotalPage() > endPage) {
            movePage = endPage + 1;
            pagiNation.append("<a class='nextMark' href='")
                      .append(erDTO.getUrl())
                      .append("?currentPage=")
                      .append(movePage);

            if (hasKeyword) {
                pagiNation.append("&keyword=").append(safeKw);
            }
            pagiNation.append("'>&gt;&gt;</a>");
        } else {
            pagiNation.append("<span class='currentPage' style='opacity:.35'>&gt;&gt;</span>");
        }

        return pagiNation.toString();
    }

    public EventAdminDomain searchOneEvent(int eventNum) {
        EventAdminDomain domain = null;
        try {
            domain = eaDAO.selectEventDetail(eventNum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return domain;
    }

    public boolean addEvent(EventAdminDTO eaDTO) {
        try {
            return eaDAO.insertEvent(eaDTO) == 1;
        } catch (SQLException | PersistenceException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifyEvent(EventAdminDTO eaDTO) {
        boolean flag = false;
        try {
            flag = eaDAO.updateEvent(eaDTO) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean removeEvent(EventAdminDTO eaDTO) {
        boolean flag = false;
        try {
            flag = eaDAO.deleteEvent(eaDTO) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    // ✅ adminId → adminNum 조회 (세션 보정용)
    public Integer findAdminNumByAdminId(String adminId) {
        if (adminId == null || adminId.trim().isEmpty()) {
            return null;
        }

        try {
            return eaDAO.selectAdminNumByAdminId(adminId.trim());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}