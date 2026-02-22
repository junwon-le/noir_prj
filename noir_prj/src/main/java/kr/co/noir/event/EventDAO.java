package kr.co.noir.event;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;
import kr.co.noir.notice.BoardRangeDTO;

@Repository("EventDAO")
public class EventDAO {

	
	public int selectBoardTotalCnt(BoardRangeDTO rDTO) throws SQLException{
		int totalCnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		totalCnt = ss.selectOne("kr.co.noir.event.selectBoardTotalCount",rDTO);
		
		if(ss !=null) {ss.close();}
		return totalCnt;
		
	}//selectBoardTotalCnt
	
	
	public List<EventDomain> selectRangeBoard(BoardRangeDTO rDTO) throws SQLException {
        List<EventDomain> list = null; 
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        
        list = ss.selectList("kr.co.noir.event.selectRangeBoard", rDTO);
        
        if(ss != null) { ss.close(); }
        return list;
    }
	   
	   
	   public EventDomain selectBoardDetail(int num) throws SQLException{
		   EventDomain rDomain = null;
		   	
			SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
			rDomain = ss.selectOne("kr.co.noir.event.selectBoardDetail",num);
			
			if(ss !=null) {ss.close();}
			return rDomain;
			
		}//selectBoardDetail
	
	
}//class
