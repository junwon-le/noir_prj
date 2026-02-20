package kr.co.noir.event;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository("eventAdminDAO")
public class EventAdminDAO {

    // 전체 게시글 수
    public int selectEventTotalCnt(EventRangeDTO erDTO) throws SQLException {
        SqlSession ss = null;
        try {
            ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
            return ss.selectOne("kr.co.noir.eventAdmin.selectEventTotalCnt", erDTO);
        } finally {
            if (ss != null) ss.close();
        }
    }

    // 이벤트 목록
    public List<EventAdminDomain> selectEventList(EventRangeDTO erDTO) throws SQLException {
        SqlSession ss = null;
        try {
            ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
            return ss.selectList("kr.co.noir.eventAdmin.selectEventList", erDTO);
        } finally {
            if (ss != null) ss.close();
        }
    }

    // 이벤트 추가
    public int insertEvent(EventAdminDTO eaDTO) throws SQLException, PersistenceException {
        SqlSession ss = null;
        try {
            ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
            return ss.insert("kr.co.noir.eventAdmin.insertEvent", eaDTO);
        } finally {
            if (ss != null) ss.close();
        }
    }

    // 이벤트 상세
    public EventAdminDomain selectEventDetail(int eventNum) throws SQLException {
        SqlSession ss = null;
        try {
            ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
            return ss.selectOne("kr.co.noir.eventAdmin.selectEventDetail", eventNum);
        } finally {
            if (ss != null) ss.close();
        }
    }

    // 이벤트 수정
    public int updateEvent(EventAdminDTO eaDTO) throws SQLException {
        SqlSession ss = null;
        try {
            ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
            return ss.update("kr.co.noir.eventAdmin.updateEvent", eaDTO);
        } finally {
            if (ss != null) ss.close();
        }
    }

    // 이벤트 삭제 (soft delete: update)
    public int deleteEvent(EventAdminDTO eaDTO) throws SQLException {
        SqlSession ss = null;
        try {
            ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
            return ss.update("kr.co.noir.eventAdmin.deleteEvent", eaDTO);
        } finally {
            if (ss != null) ss.close();
        }
    }
}
