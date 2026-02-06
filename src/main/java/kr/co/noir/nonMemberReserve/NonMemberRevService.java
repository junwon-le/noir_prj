package kr.co.noir.nonMemberReserve;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import kr.co.noir.mypageReserve.DinningRevDetailDomain;
import kr.co.noir.mypageReserve.HotelRevDetailDomain;
import kr.co.noir.mypageReserve.ReserveDetailDTO;

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
			
		}catch (PersistenceException pe) {
			pe.printStackTrace();
			
		}//end catch
		
		return flag;
		
	}//NonReserveCheck
	
	public List<HotelRevDetailDomain> searchOneHotelRevDetail(NonMemberRevDTO rmrDTO){
		List<HotelRevDetailDomain> list=null;
		TextEncryptor te = Encryptors.text(key, salt);
		try {
			
			list=nmrm.selectOneHotelDetail(rmrDTO);
			for(HotelRevDetailDomain hd: list) {
				hd.setTel(te.decrypt(hd.getTel()));
				
			}//end for
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		}//end catch
			
		
		return list;
		
	}//searchOneHotelRevDetail
	
	
	public DinningRevDetailDomain searchOneDinningRevDetail(NonMemberRevDTO rmrDTO) {
		DinningRevDetailDomain drdDomain=null;
		TextEncryptor te = Encryptors.text(key,salt);
		try {
			
			drdDomain =nmrm.selectOnedinningDetail(rmrDTO);
			drdDomain.setTel(te.decrypt(drdDomain.getTel()));
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		}//end catch
		
		return drdDomain;
		
	
	}//searchOneDinningRevDetail
	
	
	
}//class
