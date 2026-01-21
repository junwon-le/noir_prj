package kr.co.noir.reserve;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository
public class RoomReserveDAO {
	
	public List<RoomSearchDomain> selectRoom(RoomSearchDTO rsDTO) throws SQLException{
		List<RoomSearchDomain> list = null;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		list=ss.selectList("kr.co.sist.reserve.selectRoom",rsDTO);
		if(ss!=null) {	ss.close();	}
		return list;
	}
}
