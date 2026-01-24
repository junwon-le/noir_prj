package kr.co.noir.room;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository
public class RoomDAO {

	/**
	 * 사용자 숙소 보기
	 * @param num
	 * @return
	 * @throws PersistenceException
	 */
	public RoomDomain selectRoom(int num) throws PersistenceException{
		
		RoomDomain rDomain = null;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		rDomain = ss.selectOne("kr.co.noir.room.roomSelect",num);
		if(ss!=null) {
			ss.close();
		}

		return rDomain;
	}
	
	/**
	 * 사용자 숙소 상세 보기
	 * @param num
	 * @return
	 * @throws PersistenceException
	 */
	public RoomDomain selectDetailRoom(int num) throws PersistenceException{
		
		RoomDomain rDomain = null;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		rDomain = ss.selectOne("kr.co.noir.room.roomDetailSelect",num);
		if(ss!=null) {
			ss.close();
		}

		return rDomain;
	}
	
	/**
	 * 관리자 숙소 관리 페이지
	 * @param num
	 * @return
	 * @throws PersistenceException
	 */
	public List<RoomDomain> selectRoomList() throws PersistenceException{
		
		List<RoomDomain> list = null;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		list = ss.selectList("kr.co.noir.room.roomList");
		if(ss!=null) {
			ss.close();
		}

		return list;
	}
	
	public List<RoomPriceDomain> selectTodayRoomPrice() throws PersistenceException{
		
		List<RoomPriceDomain> rpDomain = null;

		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		rpDomain = ss.selectList("kr.co.noir.room.roomPriceTodayList");
		if(ss!=null) {
			ss.close();
		}		
		
		return rpDomain;
	}
	
	public List<RoomPriceDomain> selectDayRoomPrice(String date) throws PersistenceException{
		
		List<RoomPriceDomain> rpDomain = null;

		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		rpDomain = ss.selectList("kr.co.noir.room.roomPriceList",date);
		if(ss!=null) {
			ss.close();
		}		
		
		return rpDomain;
	}
	
}
