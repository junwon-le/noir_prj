package kr.co.noir.event;



import java.sql.SQLException;
import java.util.List;     

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository("eventAdminDAO")
public class EventAdminDAO {
	
	public int selectEventTotalCnt(EventRangeDTO erDTO) throws SQLException {
		int totalCnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		totalCnt = ss.selectOne("kr.co.noir.eventAdmin.selectEventTotalCnt", erDTO);

		if (ss != null) {
			ss.close();
		}
		return totalCnt;

	}// selectEventTotalCnt

	
	//이벤트 목록
	public List<EventAdminDomain> selectEventList(EventRangeDTO erDTO) throws SQLException {
		List<EventAdminDomain> list = null;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		list = ss.selectList("kr.co.noir.eventAdmin.selectEventList", erDTO);

		if (ss != null) {
			ss.close();
		}

		return list;
	}// selectRangeBoard

	
	//이벤트 추가
	public int insertEvent(EventAdminDTO eaDTO) throws PersistenceException {
	    SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
	    int cnt = ss.insert("kr.co.noir.eventAdmin.insertEvent", eaDTO);
	    if (ss != null) { ss.close(); }
	    return cnt;
	}


	
	//이벤트 상세
	public EventAdminDomain selectEventDetail(int eventNum) throws SQLException {
		EventAdminDomain eaDomain = null;

		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		eaDomain = ss.selectOne("kr.co.noir.eventAdmin.selectEventDetail", eventNum);

		if (ss != null) {
			ss.close();
		}
		return eaDomain;

	}// selectEventDetail

	
	//이벤트 업데이트
	public int updateEvent(EventAdminDTO eaDTO) throws SQLException {
		int cnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		cnt = ss.update("kr.co.noir.eventAdmin.updateEvent", eaDTO);
		if (ss != null) {
			ss.close();
		}
		return cnt;
	}// updateEvent

	
	//delete
	public int deleteEvent(EventAdminDTO eaDTO) throws SQLException {
	    int cnt = 0;
	    SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);

	    // ✅ delete → update 로
	    cnt = ss.update("kr.co.noir.eventAdmin.deleteEvent", eaDTO);

	    if (ss != null) { ss.close(); }
	    return cnt;
	}


}// class
