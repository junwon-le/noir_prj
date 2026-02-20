package kr.co.noir.review;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository("reviewAdminDAO")
public class ReviewAdminDAO {

	

	//전체 총 게시물 수
	public int selectReviewTotalCnt(ReviewRangeDTO rrDTO) throws SQLException{
		int totalCnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		totalCnt = ss.selectOne("kr.co.noir.review.ReviewMapper.selectReviewTotalCnt",rrDTO);
		
		if(ss !=null) {ss.close();}
		return totalCnt;
		
	}//selectReviewTotalCnt
	
	//객실타입으로 필터링된 리뷰의 총 개수
	public int selectRoomReviewCnt(ReviewRangeDTO rrDTO) throws SQLException{
		int totalCnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		totalCnt = ss.selectOne("kr.co.noir.review.ReviewMapper.selectRoomReviewCnt",rrDTO);
		
		if(ss !=null) {ss.close();}
		return totalCnt;
		
	}//selectReviewTotalCnt
	
	   //관리자 리뷰 목록 조회(페이징 적용)
	  //rrDTO에 들어있는 조건(검색/필터/페이징 startNum~endNum)을 가지고 리뷰 리스트를 가져옴
	   public List<ReviewAdminDomain> selectReviewList(ReviewRangeDTO rrDTO) throws SQLException{
		      List<ReviewAdminDomain> list = null; 
		      SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		      list = ss.selectList("kr.co.noir.review.ReviewMapper.selectReviewList",rrDTO);
		      if(ss !=null) {ss.close();}
		      
		      return list;
		   }//selectRangeReview

	   //roomTypeNum으로 필터된 리뷰 목록(페이징 적용)
	   //객실타입 선택 시 해당 타입의 리뷰만 보여줌
	   public List<ReviewAdminDomain> selectReviewByRoom(ReviewRangeDTO rrDTO) throws SQLException{
		   List<ReviewAdminDomain> list = null; 
		   SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		   list = ss.selectList("kr.co.noir.review.ReviewMapper.selectReviewByRoom",rrDTO);
		   if(ss !=null) {ss.close();}
		   
		   return list;
	   }//selectRangeReview
	   
	   
	   //리뷰 1건 상세 정보 조회
	   public ReviewAdminDomain selectReviewDetail(int num) throws SQLException{
		   ReviewAdminDomain raDomain = null;
		   	
			SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
			raDomain = ss.selectOne("kr.co.noir.review.ReviewMapper.selectReviewDetail",num);
			
			if(ss !=null) {ss.close();}
			return raDomain;
			
		}//selectReviewDetail
	   
		public int updateReplyReview( ReviewAdminDTO raDTO ) {
			int cnt = 0;
			SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);	
			cnt = ss.update("kr.co.noir.review.ReviewMapper.updateReplyReview", raDTO);
			if(ss!=null) {ss.close();}
			return cnt;
			
		}
		
		
		
		// ✅ 해당 리뷰 삭제(soft delete)
		public int deleteAdminReview(int reviewNum) throws SQLException {
		    int cnt = 0;
		    SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);

		    cnt = ss.update("kr.co.noir.review.ReviewMapper.deleteAdminReview", reviewNum);

		    if (ss != null) { ss.close(); }
		    return cnt;
		}

		// 답변만 삭제
		public int deleteOnlyReply(int reviewNum) throws SQLException {
		    int cnt = 0;
		    SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);

		    cnt = ss.update("kr.co.noir.review.ReviewMapper.deleteOnlyReply", reviewNum);

		    if (ss != null) { ss.close(); }
		    return cnt;
		}
		
		
		// ✅ 리뷰 이미지 리스트 조회 (review_img 테이블)
		public List<String> selectReviewImgList(int reviewNum) throws SQLException {
		    List<String> imgList = null;
		    SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);

		    imgList = ss.selectList("kr.co.noir.review.ReviewMapper.selectReviewImgList", reviewNum);

		    if (ss != null) { ss.close(); }
		    return imgList;
		}

		
}//class
