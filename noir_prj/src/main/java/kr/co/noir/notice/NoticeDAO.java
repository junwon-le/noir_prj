package kr.co.noir.notice;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository
public class NoticeDAO {

	
	public int selectBoardTotalCnt(BoardRangeDTO rDTO) throws SQLException{
		int totalCnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		totalCnt = ss.selectOne("kr.co.noir.notice.selectBoardTotalCnt",rDTO);
		
		if(ss !=null) {ss.close();}
		return totalCnt;
		
	}//selectBoardTotalCnt
	
	
	   public List<NoticeDomain> selectRangeBoard(BoardRangeDTO rDTO) throws SQLException{
		      List<NoticeDomain> list = null; 
		      SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		      list = ss.selectList("kr.co.noir.notice.selectRangeBoard",rDTO);
		      if(ss !=null) {ss.close();}
		      
		      return list;
		   }//selectRangeBoard
	   
	   
	   
	   public NoticeDomain selectBoardDetail(int noticeNum) throws SQLException{
		   NoticeDomain nDomain = null;
		   	
			SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
			nDomain = ss.selectOne("kr.co.noir.notice.selectBoardDetail",noticeNum);
			
			if(ss !=null) {ss.close();}
			return nDomain;
			
		}//selectBoardDetail
	
	
}//class
