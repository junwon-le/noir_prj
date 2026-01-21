package kr.co.noir.room;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
	
	@Autowired
	private RoomDAO rDAO;
	
	
	public RoomDomain searchRoom(int num) {
		RoomDomain rDomain = null;
		try {
			rDomain = rDAO.selectRoom(num);
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
		
		return rDomain; 
	}
	
	public RoomDomain searchDetailRoom(int num) {
		RoomDomain rDomain = null;
		try {
			rDomain = rDAO.selectDetailRoom(num);
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
		
		return rDomain; 
	}
	
}
