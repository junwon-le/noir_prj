package kr.co.noir.reserve;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomReserveService {
	
	@Autowired(required = false)
	private RoomReserveDAO rrDAO;
	
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
	
}

