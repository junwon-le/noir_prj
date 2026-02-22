package kr.co.noir.notice;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository("noticeAdminDAO")
public class NoticeAdminDAO {
	


	public int selectNoticeTotalCnt(BoardRangeDTO rDTO) throws SQLException{
		int totalCnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		totalCnt = ss.selectOne("kr.co.noir.noticeAdmin.selectNoticeTotalCnt",rDTO);
		
		if(ss !=null) {ss.close();}
		return totalCnt;
		
	}//selectNoticeTotalCnt
	
	
	   public List<NoticeAdminDomain> selectNoticeList(BoardRangeDTO rDTO) throws SQLException{
		      List<NoticeAdminDomain> list = null; 
		      SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		      list = ss.selectList("kr.co.noir.noticeAdmin.selectNoticeList",rDTO);
		      if(ss !=null) {ss.close();}
		      
		      return list;
		   }//selectNoticeList
	   
	   //////확인
	   
	   public NoticeAdminDomain selectNoticeDetail(int noticeNum) throws SQLException{
		   NoticeAdminDomain nDomain = null;
		   	
			SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
			nDomain = ss.selectOne("kr.co.noir.noticeAdmin.selectNoticeDetail",noticeNum);
			
			if(ss !=null) {ss.close();}
			return nDomain;
			
		}//selectNoticeDetail
	   

		public void insertNotice(NoticeAdminDTO naDTO) throws PersistenceException {
			// 1. MyBatis Handler 얻기
			SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);

			// 2. 쿼리문 수행 후 결과 얻기
			ss.insert("kr.co.noir.noticeAdmin.insertNotice", naDTO);
			// 3. 결과 작업
			// 4.MyBatis Handler 닫기
			if (ss != null) {
				ss.close();
			} // end if

		}// insertBoard


		public int updateNotice(NoticeAdminDTO naDTO) throws SQLException {
			int cnt = 0;
			SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
			cnt = ss.update("kr.co.noir.noticeAdmin.updateNotice", naDTO);
			if (ss != null) {
				ss.close();
			}
			return cnt;
		}// updateNotice

		public int deleteNotice( int noticeNum ) throws SQLException {

			int cnt = 0;
			SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
			cnt = ss.update("kr.co.noir.noticeAdmin.deleteNotice", noticeNum);
			if (ss != null) {
				ss.close();
			}

			return cnt;

		}// deleteNotice		



	
	}

