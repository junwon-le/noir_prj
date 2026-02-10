package kr.co.noir.mypage;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;
import kr.co.noir.event.EventDomain;

@Repository
public class MypageDAO {

	
	//호텔 예약 count 근데 3개월이면 되나..
	public int selectHotelRevCnt(String id) throws PersistenceException {
		int cnt = 0;
		
		
		SqlSession ss =MyBatisHandler.getInstance().getMyBatisHandler(false);
		cnt=ss.selectOne("kr.co.noir.mypage.hotelRevCnt",id);
		
		System.out.println("dao---"+cnt);
	  	if(ss!=null) {ss.close();}//end if
		
		return cnt;

	}//selectHotelRevCnt
	
	
	
	//user 이름 가져오는 DAO
	public String selectMemberName(String id) throws PersistenceException {
		
		String name ="";
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		name=ss.selectOne("kr.co.noir.mypage.memberName",id);
		
		if(ss!=null) {ss.close();}//end if
		return name;
		
	}//selectMemberName

	
	//해당 user에 dinning예약건수 DAO
	public int  selectDinningRevCnt(String id) throws PersistenceException  {
		int cnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		cnt=ss.selectOne("kr.co.noir.mypage.dinningRevCnt",id);
		
		System.out.println("------DAO"+cnt);
		
		if(ss!=null) {ss.close();}//end if
		return cnt;
		
	}//selectDinningRevCnt
	
	//eventList 랜덤으로 3개 가져오기 맞춤형 같이 느낌있게;;
	public List<EventDomain> selectEventList() throws PersistenceException{
		List<EventDomain> list = new ArrayList<EventDomain>();
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		list=ss.selectList("kr.co.noir.mypage.eventList");
		
		System.out.println(list);
		if(ss!=null) {ss.close();}//end if
		return list;
		
	}//selectEventList

	
}//class
