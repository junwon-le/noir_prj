package kr.co.noir.inquiry;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;
import kr.co.noir.notice.BoardRangeDTO;

@Repository
public class InquiryDAO {

	public List<InquiryDomain> selectInquiryByMember(int memberNum) throws SQLException{
		List<InquiryDomain> list = null; 
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		list = ss.selectList("kr.co.noir.inquiry.selectInquiryByMember");
		if(ss !=null) {ss.close();}
		
		return list;
	}
	
	
	public void insertInquiryByMember(InquiryDTO iDTO) throws SQLException{
		
	}
	
	public int selectBoardTotalCnt(BoardRangeDTO rDTO) throws SQLException{
		int totalCnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		totalCnt = ss.selectOne("kr.co.noir.notice.selectBoardTotalCnt",rDTO);
		
		if(ss !=null) {ss.close();}
		return totalCnt;
		
	}//selectBoardTotalCnt
	
	
	   public List<InquiryDomain> selectRangeInquiry(BoardRangeDTO rDTO) throws SQLException{
		      List<InquiryDomain> list = null; 
		      SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		      list = ss.selectList("kr.co.noir.notice.selectRangeBoard",rDTO);
		      if(ss !=null) {ss.close();}
		      
		      return list;
		   }//selectRangeBoard
	   
	   
	   
	   public InquiryDomain selectBoardDetail(int noticeNum) throws SQLException{
		   InquiryDomain nDomain = null;
		   	
			SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
			nDomain = ss.selectOne("kr.co.noir.notice.selectBoardDetail",noticeNum);
			
			if(ss !=null) {ss.close();}
			return nDomain;
			
		}//selectBoardDetail
	
	
}//class
