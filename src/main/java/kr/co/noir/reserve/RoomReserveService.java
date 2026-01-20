package kr.co.noir.reserve;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomReserveService {
	
	@Autowired
	private RoomReserveDAO rrDAO;
	
	public List<RoomSearchDomain> searchRoom(){
		List<RoomSearchDomain> list = null;
		
		try {
			list=rrDAO.selectRoom();
		}catch(SQLException se) {
			se.printStackTrace();
		}
		return list;
	}
}

