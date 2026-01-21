package kr.co.noir.mypage;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository
public class MypageDAO {

	public int selectHotelRevCnt(String id) throws PersistenceException {
		int cnt = 0;
		
		
		SqlSession ss =MyBatisHandler.getInstance().getMyBatisHandler(false);
		cnt=ss.selectOne("kr.co.noir.mypage.hotelRevCnt",id);
		
		System.out.println("dao---"+cnt);
	  	if(ss!=null) {ss.close();}//end if
		
		return cnt;

	}//selectHotelRevCnt

	public int  selectDinningRevCnt(String id) {
		int cnt = 0;
		
		return cnt;
		
	}//selectDinningRevCnt

	
}//class
