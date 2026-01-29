package kr.co.noir.reserve;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.siot.IamportRestClient.IamportClient;

@Service
public class RoomReserveService {
	
	@Autowired(required = false)
	private RoomReserveDAO rrDAO;
	
	@Autowired
	private IamportClient client;
	
	
	public List<RoomSearchDomain> searchRoom(RoomSearchDTO rsDTO){
		List<RoomSearchDomain> list = null;
		
		try {
			list=rrDAO.selectRoom(rsDTO);
		}catch(SQLException se) {
			se.printStackTrace();
		}
		return list;
	}//searchRoom
	
	public MemberDomain searchMember(String id){
		MemberDomain memberDomain = null;
		
		try {
			memberDomain=rrDAO.selectMember(id);
			String email = memberDomain.getMember_email();
			memberDomain.setMember_email_id(email.substring(0,email.indexOf('@')));
			memberDomain.setMember_email_domain(email.substring(email.indexOf('@')+1));
		}catch(SQLException se) {
			se.printStackTrace();
		}
		return memberDomain;
	}//searchMember
	
	public boolean addRoomDepending(List<RoomDependingDTO> rpDTO) {
		boolean flag = false;
		try{
		rrDAO.insertRoomDepending(rpDTO);
		flag =true;
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
		return flag;
	}//addRoomDepending
	
	
	public int deleteDepending(String id) {
		int cnt=0;		
		try{
			cnt = rrDAO.deleteDepending(id);
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
		return cnt;
	}
	
	public void addRoomReserve(PayInfoDTO pDTO, RoomReserveDTO rrDTO ) {
		try {
	        // 빌링키 정보를 조회
	        var response = client.getBillingCustomer(pDTO.getBilling_key());
	        System.out.println("카드사명: " + response.getResponse().getCardName());
	        System.out.println("빌링키: " + pDTO.getBilling_key());
	    } catch (Exception e) {
	    }
		//예약 테이블 데이터 처리
		MemberDomain memberDomain=searchMember(rrDTO.getUser_id());
		rrDTO.setUser_num(memberDomain.getMember_num());  
		String startDate = rrDTO.getStartDate().replaceAll("\\(.*?\\)", "").trim();
		String endDate = rrDTO.getEndDate().replaceAll("\\(.*?\\)", "").trim();
		rrDTO.setRoom_res_startDate(LocalDate.parse(startDate));
		rrDTO.setRoom_res_endDate(LocalDate.parse(endDate));
		
		//숙소 예약 테이블 데이터 처리
		
		//예약 정보 저장 트렌젝션 처리
		rrDAO.insertRoomReserve(pDTO, rrDTO);
	}

	
}

