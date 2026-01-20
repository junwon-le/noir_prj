package kr.co.noir.room;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository
public class RoomDAO {

	public RoomDomain selectRoom(int num) throws PersistenceException{
		
		RoomDomain rDomain = null;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		rDomain = ss.selectOne("kr.co.noir.room.roomSelect",num);
		if(ss!=null) {
			ss.close();
		}

		return rDomain;
	
		
	}
	
}
