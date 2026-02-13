package kr.co.noir.reserve;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
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
	}//selectRoom
	
	public List<RoomSearchDomain> selectRoomServer(RoomSearchDTO rsDTO) throws SQLException{
		List<RoomSearchDomain> list = null;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		list=ss.selectList("kr.co.sist.reserve.selectRoomServer",rsDTO);
		if(ss!=null) {	ss.close();	}
		return list;
	}//selectRoomServer
	
	public MemberDomain selectMember(String id) throws SQLException{
		MemberDomain memberDomain = null;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		memberDomain=ss.selectOne("kr.co.sist.reserve.selectMember",id);
		if(ss!=null) {	ss.close();	}
		return memberDomain;
	}//selectMember
	
	public void insertRoomDepending(List<RoomDependingDTO> rpList) throws PersistenceException{
		
		for(RoomDependingDTO rpDTO:rpList) {
			
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		ss.insert("kr.co.sist.reserve.insertRoomPending",rpDTO);
		
		if(ss!=null) {	ss.close();	}
		}//end for
		
	}//insertRoomDepending
	
	public int deleteDepending(String id) throws PersistenceException{
		int cnt=0;	
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		cnt = ss.delete("kr.co.sist.reserve.deletePending",id);
		if(ss!=null) {	ss.close();	}
		return cnt; 
	}//deleteDepending
	
	public int insertRoomReserve(PayInfoDTO pDTO, RoomReserveDTO rrDTO) throws PersistenceException {
		int cnt =0;
		int ok= 1+rrDTO.getRoom_type().size()+1+1;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		//예약 테이블에 추가
		cnt = ss.insert("kr.co.sist.reserve.insertRoomReserve",rrDTO);
		//숙소 예약 테이블 추가
		for(int room_num : rrDTO.getRoom_type()) {
			rrDTO.setTemp_room_type(room_num);
			cnt += ss.insert("kr.co.sist.reserve.insertParlor",rrDTO);
		}
		//결제 테이블 추가
		pDTO.setReserve_num(rrDTO.getReserve_num());//예약번호
		cnt += ss.insert("kr.co.sist.reserve.insertPay",pDTO);
		//결제 정보 테이블 추가
		cnt += ss.insert("kr.co.sist.reserve.insertPayInfo",pDTO);
		if(cnt == ok) {	
			ss.commit();
			ss.close();	
		}else {
			ss.rollback();
		}
	
		
		return cnt;
	}//insertRoomReserve
	
	public int insertNonRoomReserve(PayInfoDTO pDTO, RoomReserveDTO rrDTO) throws PersistenceException {
		int cnt =0;
		int ok= rrDTO.getRoom_type().size()+4;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		//비회원 테이블에 추가
		cnt += ss.insert("kr.co.sist.reserve.insertNonMember",rrDTO);
		//예약 테이블에 추가
		cnt += ss.insert("kr.co.sist.reserve.insertNonRoomReserve",rrDTO);
		//숙소 예약 테이블 추가
		for(int room_num : rrDTO.getRoom_type()) {
			rrDTO.setTemp_room_type(room_num);
			cnt += ss.insert("kr.co.sist.reserve.insertParlor",rrDTO);
		}
		//결제 테이블 추가
		pDTO.setReserve_num(rrDTO.getReserve_num());//예약번호
		cnt += ss.insert("kr.co.sist.reserve.insertPay",pDTO);
		//결제 정보 테이블 추가
		cnt += ss.insert("kr.co.sist.reserve.insertPayInfo",pDTO);
		if(cnt == ok) {	
			ss.commit();
			ss.close();	
		}else {
			ss.rollback();
		}
		
		
		return cnt;
	}//insertNonRoomReserve
	
	public void deleteNonRoomReserve() {
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		ss.delete("kr.co.sist.reserve.deleteNonRoomReserve");
		if(ss!=null) {	ss.close();	}
	}//deleteNonRoomReserve
	public void deleteNonDinningReserve() {
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		ss.delete("kr.co.sist.reserve.deleteNonDinningReserve");
		if(ss!=null) {	ss.close();	}
	}//deleteNonDinningReserve
	
	
	
}
