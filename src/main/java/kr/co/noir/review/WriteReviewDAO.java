package kr.co.noir.review;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository
public class WriteReviewDAO {
	
	public int insertReview(WriteReviewDTO wrDTO) throws SQLException{
		
		int result = 0;
		SqlSession ss=MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		try {
			result = ss.insert("kr.co.noir.review.insertReview",wrDTO);
			if(result>0) {
				ss.commit();
			}
		}catch(Exception e) {
			ss.rollback();
			throw e;
		}finally {
			if(ss!=null) {ss.close();}
		}
		
		return result;
		
	}
	
	public int insertReviewImg(WriteReviewDTO wrDTO) throws SQLException {
	    int result = 0;
	    SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
	    try {
	        result = ss.insert("kr.co.noir.review.insertReviewImg", wrDTO);
	        if (result > 0) ss.commit();
	    } catch (Exception e) {
	        ss.rollback();
	        throw e;
	    } finally {
	        if (ss != null) ss.close();
	    }
	    return result;
	}
	
	
	public List<WriteReviewDTO> getUnreviewedRooms(int memberNum) {
	    SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
	    List<WriteReviewDTO> list = null;
	    
	    try {
	        list = ss.selectList("kr.co.noir.review.getUnreviewedRooms", memberNum);
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        if (ss != null) ss.close();
	    }
	    
	    return list;
	}
	
}
