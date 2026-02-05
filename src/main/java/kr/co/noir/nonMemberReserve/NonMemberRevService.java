package kr.co.noir.nonMemberReserve;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class NonMemberRevService {

	@Autowired(required = false)
	private NonMemberRevMapper nmrm;
	
	@Value("${user.crypto.key}")
	private String key;

	@Value("${user.crypto.salt}")
	private String salt;
	
	
	
	public boolean NonReserveCheck(NonMemberRevDTO rmrDTO) {
		boolean flag=false;
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		try {
			
			
			String password=nmrm.nonMemberRevCheck(rmrDTO);
			String nowPassword= rmrDTO.getPassword();
			
			flag=bpe.matches(nowPassword, password);
			System.out.println("비밀번호 결과 : "+flag);
			
		}catch (PersistenceException pe) {
			pe.printStackTrace();
			
		}//end catch
		
		return flag;
		
	}//NonReserveCheck
	
}//class
