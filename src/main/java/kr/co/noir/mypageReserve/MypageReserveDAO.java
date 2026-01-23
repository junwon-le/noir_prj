package kr.co.noir.mypageReserve;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository
public class MypageReserveDAO {

	public List<HotelRevSearchDomain> selectHotelRevList(ReserveSearchDTO rsDTO) throws PersistenceException{
		List<HotelRevSearchDomain> list = new ArrayList<HotelRevSearchDomain>();
		SqlSession ss= MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		list=ss.selectList("kr.co.noir.mypageReserve.hotelRevList",rsDTO);
		
		return list;
		
	}//selectHotelRevList
	
}
