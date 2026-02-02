package kr.co.noir.reserve;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import com.siot.IamportRestClient.IamportClient;

import kr.co.noir.room.RoomDAO;

@Service
public class RoomReserveService {
	
	@Value("${user.crypto.key}")
	private String key;
	
	@Value("${user.crypto.salt}")
	private String salt;
	
	@Autowired(required = false)
	private RoomReserveDAO rrDAO;
	
	@Autowired(required = false)
	private RoomDAO rDAO;
	
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
	
	public List<RoomSearchDomain> searchRoomServer(RoomSearchDTO rsDTO){
		List<RoomSearchDomain> list = null;
		
		try {
			list=rrDAO.selectRoomServer(rsDTO);
		}catch(SQLException se) {
			se.printStackTrace();
		}
		return list;
	}//searchRoomServer
	
	public MemberDomain searchMember(String id){
		MemberDomain memberDomain = null;
		
		try {
			memberDomain=rrDAO.selectMember(id);
			TextEncryptor te = Encryptors.text(key,salt);
			memberDomain.setMember_tel(te.decrypt(memberDomain.getMember_tel()));
			String email=te.decrypt(memberDomain.getMember_email());
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
	
	public boolean addRoomReserve(PayInfoDTO pDTO, RoomReserveDTO rrDTO ) {
		
		//예약한 날짜에 방이 있는 지 서버에서 검증
		String startDate = rrDTO.getStartDate().replaceAll("\\(.*?\\)", "").trim();
		String endDate = rrDTO.getEndDate().replaceAll("\\(.*?\\)", "").trim();
		boolean flag = false;
		if(!(flag = roomCheck(startDate,endDate,pDTO,rrDTO))){
			return flag;
		};
		
		
		//예약 테이블 데이터 처리 - 서버에서 다시 db조회하여 실제 데이터를 가져옴
		MemberDomain memberDomain=searchMember(rrDTO.getUser_id());
		rrDTO.setUser_num(memberDomain.getMember_num());
		
		TextEncryptor te= Encryptors.text(key, salt);
		rrDTO.setRoom_res_startDate(LocalDate.parse(startDate));
		rrDTO.setRoom_res_endDate(LocalDate.parse(endDate));
		rrDTO.setEmail(te.encrypt(rrDTO.getEmailId()+"@"+rrDTO.getEmailDomain()));
		rrDTO.setReserve_tel(te.encrypt(rrDTO.getReserve_tel()));
		//숙소 예약 테이블 데이터 처리
		//결제 정보 데이터 처리
		try {
			// 빌링키 정보를 조회
			var response = client.getBillingCustomer(pDTO.getBilling_key());
			pDTO.setAgency(response.getResponse().getCardName());;//카드사
			pDTO.setCard_number(response.getResponse().getCardNumber());//카드 번호 
			pDTO.setPg_provider(response.getResponse().getPgProvider());//PG사 구분 코드
		} catch (Exception e) {
		}
		try {
		//예약 정보 저장 트렌젝션 처리
		flag = rrDAO.insertRoomReserve(pDTO, rrDTO)==(3+rrDTO.getRoom_type().size());
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
		return flag;
	}//addRoomReserve
	
	public boolean addNonRoomReserve(PayInfoDTO pDTO, RoomReserveDTO rrDTO ) {
		
		//예약한 날짜에 방이 있는 지 서버에서 검증
		String startDate = rrDTO.getStartDate().replaceAll("\\(.*?\\)", "").trim();
		String endDate = rrDTO.getEndDate().replaceAll("\\(.*?\\)", "").trim();
		
		boolean flag = false;
		if(!(flag = roomCheck(startDate,endDate,pDTO,rrDTO))){
			return flag;
		};
		
		
		//예약 테이블 데이터 처리 - 서버에서 다시 db조회하여 실제 데이터를 가져옴
		rrDTO.setRoom_res_startDate(LocalDate.parse(startDate));
		rrDTO.setRoom_res_endDate(LocalDate.parse(endDate));
		String email = rrDTO.getEmailId()+"@"+rrDTO.getEmailDomain();
		rrDTO.setEmail(email);
		//숙소 예약 테이블 데이터 처리
		//결제 정보 데이터 처리
		try {
			// 빌링키 정보를 조회
			var response = client.getBillingCustomer(pDTO.getBilling_key());
			pDTO.setAgency(response.getResponse().getCardName());;//카드사
			pDTO.setCard_number(response.getResponse().getCardNumber());//카드 번호 
			pDTO.setPg_provider(response.getResponse().getPgProvider());//PG사 구분 코드
		} catch (Exception e) {
		}
		try {
			//예약 정보 저장 트렌젝션 처리
			flag = rrDAO.insertNonRoomReserve(pDTO, rrDTO)==(4+rrDTO.getRoom_type().size());
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
		return flag;
	}//addNonomReserve
	public boolean roomCheck(String startDate, String endDate,PayInfoDTO pDTO, RoomReserveDTO rrDTO) {
		boolean flag = false;
		RoomSearchDTO rsDTO = new RoomSearchDTO();
		rsDTO.setStartDate(startDate);
		rsDTO.setEndDate(endDate);
		List<RoomSearchDomain> list= searchRoomServer(rsDTO);
		int[] roomArr = {0,0,0,0,0};
		int roomNum =0;
		int maxPerson =0;
		for(int room : rrDTO.getRoom_type()) {
			roomNum = room-1;
			switch(room) {
			case 1: roomArr[roomNum]+=1; break;
			case 2: roomArr[roomNum]+=1; break;
			case 3: roomArr[roomNum]+=1; break;
			case 4: roomArr[roomNum]+=1; break;
			case 5: roomArr[roomNum]+=1; break;
			}//end switch
			maxPerson += rDAO.selectRoom(room).getRoomMaxPerson();
		}//end for
		for(int i=0;i < roomArr.length; i++) {
			if(roomArr[i]> list.get(i).getAvailable_count()) {
				return flag;
			}//end if
		}//end for
		//선택한 방에 인원 유효성 검증
		if(maxPerson>rrDTO.getReserve_adult_count()) {
			return flag;
		}//end if
		//어린이 인원 수 검증
		if(rrDTO.getReserve_kid_count()>rrDTO.getRoom_type().size()*2) {
			return flag;
		}//end if
		flag=true;
		return flag;
	}//roomCheck
	
}//class

