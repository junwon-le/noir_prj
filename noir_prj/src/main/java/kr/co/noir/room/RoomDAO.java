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
	
	public List<RoomPriceDomain> selectMonthRoomPrice(RoomPriceDTO rpDTO) throws PersistenceException{
		
		List<RoomPriceDomain> rpDomain = null;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		rpDomain = ss.selectList("kr.co.noir.room.selectMonthRoom",rpDTO);
		if(ss!=null) {
			ss.close();
		}		
		
		return rpDomain;
	}
	
	public RoomPriceDomain selectCheckRoomPrice(RoomPriceDTO rpDTO) throws PersistenceException{
		
		RoomPriceDomain rpDomain = null;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		rpDomain = ss.selectOne("kr.co.noir.room.selectCheckRoom",rpDTO);
		if(ss!=null) {
			ss.close();
		}		
		
		return rpDomain;
	}
	
	
	
	public int updateRoom(RoomDTO rDTO) {
		int cnt = 0;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		cnt += ss.update("kr.co.noir.room.roomModify",rDTO);
		cnt += ss.update("kr.co.noir.room.amenityModify",rDTO);
		cnt += ss.update("kr.co.noir.room.roomServiceModify",rDTO);
		
		if(cnt==3) {
			ss.commit();
		}else {
			ss.rollback();
		}
		
		if(ss!=null) {
			ss.close();
		}
		
		return cnt;
	}
	
	public int updateRoomPrice(RoomPriceDTO rpDTO) {
		int cnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		cnt = ss.update("kr.co.noir.room.roomPriceModify",rpDTO);
		
		if(cnt==1) {
			ss.commit();			
		}else {
			ss.rollback();
		}
		
		if(ss!=null) {
			ss.close();			
		}
		
		return cnt;
	}
	
	public int insertRoomPrice(RoomPriceDTO rpDTO) {
		int cnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		cnt = ss.update("kr.co.noir.room.roomPriceInsert",rpDTO);
		
		if(cnt==1) {
			ss.commit();			
		}else {
			ss.rollback();
		}
		
		if(ss!=null) {
			ss.close();			
		}
		
		return cnt;
	}
}
