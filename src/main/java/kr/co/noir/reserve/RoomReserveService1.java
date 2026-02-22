package kr.co.noir.reserve;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.siot.IamportRestClient.IamportClient;

import kr.co.noir.room.RoomDAO;

@Service
public class RoomReserveService1 {
	
	@Value("${user.crypto.key}")
	private String key;
	
	@Value("${user.crypto.salt}")
	private String salt;
	
	@Autowired(required = false)
	private RoomReserveMapper rrm;
	
	public List<RoomSearchDomain1> searchRoom1(RoomSearchDTO1 rsDTO){
		List<RoomSearchDomain1> list = null;
		
		try {
			list=rrm.selectRoom1(rsDTO);
		}catch(PersistenceException se) {
			se.printStackTrace();
		}
		return list;
	}//searchRoom
}//class

