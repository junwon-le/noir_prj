package kr.co.noir.review;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;
import kr.co.noir.notice.BoardRangeDTO;

@Repository("ReviewDAO")
public class ReviewDAO {

   
   public int selectBoardTotalCnt(BoardRangeDTO rDTO) throws SQLException{
      int totalCnt = 0;
      SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
      totalCnt = ss.selectOne("kr.co.noir.review.ReviewMapper.selectBoardTotalCnt",rDTO);
      
      if(ss !=null) {ss.close();}
      return totalCnt;
      
   }//selectBoardTotalCnt

   public int selectRoomReviewCnt(BoardRangeDTO rDTO) throws SQLException{
      int totalCnt = 0;
      SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
      totalCnt = ss.selectOne("kr.co.noir.review.ReviewMapper.selectRoomReviewCnt",rDTO);
      
      if(ss !=null) {ss.close();}
      return totalCnt;
      
   }//selectBoardTotalCnt
   
   
      public List<ReviewDomain> selectReviewByMember(BoardRangeDTO rDTO) throws SQLException{
            List<ReviewDomain> list = null; 
            SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
            list = ss.selectList("kr.co.noir.review.ReviewMapper.selectReviewByMember",rDTO);
            if(ss !=null) {ss.close();}
            
            return list;
         }//selectRangeBoard

      public List<ReviewDomain> selectReviewByRoom(BoardRangeDTO rDTO) throws SQLException{
         List<ReviewDomain> list = null; 
         SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
         list = ss.selectList("kr.co.noir.review.ReviewMapper.selectReviewByRoom",rDTO);
         if(ss !=null) {ss.close();}
         
         return list;
      }//selectRangeBoard
      
      
      public ReviewDomain selectBoardDetail(int num) throws SQLException{
         ReviewDomain rDomain = null;
            
         SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
         rDomain = ss.selectOne("kr.co.noir.review.ReviewMapper.selectBoardDetail",num);
         
         if(ss !=null) {ss.close();}
         return rDomain;
         
      }//selectBoardDetail
   
   
}//class
