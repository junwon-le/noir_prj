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
    private EventAdminDAO eaDAO; // ✅ required=false 제거

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

    public int totalPage(int totalCount, int pageScale) {
        return (int) Math.ceil((double) totalCount / pageScale);
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
            list = eaDAO.selectEventList(erDTO);
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
            String title = domain.getEventTitle();
            if (title != null && title.length() > 20) {
                domain.setEventTitle(title.substring(0, 20) + "...");
            }
        }
    }

    public String pagination(EventRangeDTO erDTO) {
        StringBuilder pagiNation = new StringBuilder();

        int pageNumber = 3;
        int startPage = ((erDTO.getCurrentPage() - 1) / pageNumber) * pageNumber + 1;
        int endPage = (((startPage - 1) + pageNumber) / pageNumber) * pageNumber;

        if (erDTO.getTotalPage() <= endPage) {
            endPage = erDTO.getTotalPage();
        }

        // ✅ keyword 공백 방어 (여기서 1번만!)
        String kw = erDTO.getKeyword();
        boolean hasKeyword = kw != null && !kw.trim().isEmpty();
        String safeKw = hasKeyword ? kw.trim() : "";

        int movePage = 0;

        // 이전
        StringBuilder prevMark = new StringBuilder("[&lt;&lt;]");
        if (erDTO.getCurrentPage() > pageNumber) {
            movePage = startPage - 1;
            prevMark.setLength(0);
            prevMark.append("[<a href='").append(erDTO.getUrl()).append("?currentPage=").append(movePage);

            if (hasKeyword) {
                prevMark.append("&field=").append(erDTO.getField())
                        .append("&keyword=").append(safeKw);
            }
            prevMark.append("' class='prevMark'>&lt;&lt;</a>]");
        }

        // 페이지 링크
        StringBuilder pageLink = new StringBuilder();
        movePage = startPage;
        while (movePage <= endPage) {
            if (movePage == erDTO.getCurrentPage()) {
                pageLink.append("[ <span class='currentPage'>").append(movePage).append("</span>]");
            } else {
                pageLink.append("[ <a class='notCurrentPage' href='")
                        .append(erDTO.getUrl()).append("?currentPage=").append(movePage);

                if (hasKeyword) {
                    pageLink.append("&field=").append(erDTO.getField())
                            .append("&keyword=").append(safeKw);
                }
                pageLink.append("'>").append(movePage).append("</a>]");
            }
            movePage++;
        }

        // 다음
        StringBuilder nextMark = new StringBuilder("[&gt;&gt;]");
        if (erDTO.getTotalPage() > endPage) {
            movePage = endPage + 1;
            nextMark.setLength(0);
            nextMark.append("[ <a class='nextMark' href='").append(erDTO.getUrl())
                    .append("?currentPage=").append(movePage);

            if (hasKeyword) {
                nextMark.append("&field=").append(erDTO.getField())
                        .append("&keyword=").append(safeKw);
            }
            nextMark.append("'> &gt;&gt; </a> ]");
        }

        pagiNation.append(prevMark).append("...").append(pageLink).append("...").append(nextMark);
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

    // ✅ throws SQLException 제거 + SQLException까지 여기서 처리
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
}