package kr.co.noir.room;

import java.util.List;

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
	
	public List<RoomDomain> searchRoomList(){
		List<RoomDomain> list=null;
		try {
			list = rDAO.selectRoomList();
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
		
		
		return list;		
	}
	public List<RoomPriceDomain> searchTodayRoomPrice(){
		
		List<RoomPriceDomain> rpDomain = null;
		
		try {
			rpDomain = rDAO.selectTodayRoomPrice();
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
		
		
		return rpDomain;		
	}
	
	public List<RoomPriceDomain> searchDayRoomPrice(String date){
		
		List<RoomPriceDomain> rpDomain = null;
		
		try {
			rpDomain = rDAO.selectDayRoomPrice(date);
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
		
		
		return rpDomain;		
	}
	
}
