package kr.co.noir.inquiry;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;
import kr.co.noir.notice.BoardRangeDTO;

@Repository("inquiryDAO")
public class InquiryDAO {

	
	public int selectBoardTotalCnt(BoardRangeDTO rDTO) throws SQLException{
		int totalCnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		totalCnt = ss.selectOne("kr.co.noir.inquiry.selectBoardTotalCnt",rDTO);
		
		if(ss !=null) {ss.close();}
		return totalCnt;
		
	}//selectBoardTotalCnt
	
	
	   public List<InquiryDomain> selectRangeBoard(BoardRangeDTO rDTO) throws SQLException{
		      List<InquiryDomain> list = null; 
		      SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		      list = ss.selectList("kr.co.noir.inquiry.selectInquiryByMember",rDTO);
		      if(ss !=null) {ss.close();}
		      
		      return list;
		   }//selectRangeBoard
	   
	   public void insertInquiryByMember(InquiryDTO iDTO) throws SQLException{
			//1. MyBatis Handler 얻기
		   		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		   	
		   	//2. 쿼리문 수행 후 결과 얻기
		   		ss.insert("kr.co.noir.inquiry.insertInquiryByMember",iDTO);
		    //3. 결과 작업
		    //4.MyBatis Handler 닫기 
		   		if(ss!=null) {ss.close();}//end if
		   
		   
		   
	   }//insertBoard
	   
	   
	   public InquiryDomain selectBoardDetail(int num) throws SQLException{
		   InquiryDomain bDomain = null;
		   	
			SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
			bDomain = ss.selectOne("kr.co.noir.inquiry.selectBoardDetail",num);
			
			if(ss !=null) {ss.close();}
			return bDomain;
			
		}//selectBoardDetail
	   
	   public InquiryDomain selectMemberInfo(int memberNum) throws SQLException {
		    InquiryDomain memberInfo = null;
		    
		    // 1. MyBatis Handler 얻기 (조회니까 false - 오토커밋 상관없음)
		    SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		    
		    // 2. 쿼리 수행 (XML에 적은 id와 매칭)
		    memberInfo = ss.selectOne("kr.co.noir.inquiry.selectMemberInfo", memberNum);
		    
		    // 3. 핸들러 닫기
		    if(ss != null) { 
		        ss.close(); 
		    }
		    
		    return memberInfo;
		}
	
	
	
	
	
	
	
	
	
}//class
