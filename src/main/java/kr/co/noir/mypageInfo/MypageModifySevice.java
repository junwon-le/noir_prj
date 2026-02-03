package kr.co.noir.mypageInfo;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import kr.co.noir.login.MemberDTO;

@Service
public class MypageModifySevice {
	@Autowired(required = false)
	MypageModifyDAO mmDAO;
	
	@Value("${user.crypto.key}")
	private String key;

	@Value("${user.crypto.salt}")
	private String salt;
	
	
	/**
	 * 회원정보수정하기전 비밀번호 확인
	 * @param pcDTO
	 * @return
	 */
	public boolean searchPasswordCheck(PasswordCheckDTO pcDTO) {
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		boolean flag = false;
		
		try {
			String DBPass= mmDAO.selectPasswordCheck(pcDTO);
			String nowPass=pcDTO.getCurrentPassword();
			System.out.println(DBPass+"/"+nowPass);
			
			
			flag=bpe.matches(nowPass, DBPass);
			System.out.println(flag);
			
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		}//end catch
		
		
		return flag;
		
	}//searchPasswordCheck
	
	
	
//	public boolean loginChk(MemberDTO mDTO) {
//		boolean flag = false;
//		
//		if(mDTO!=null && mDTO.getMemberId()!=null) {
//			flag=true;
//			
//		}//end if
//		return flag;
//		
//	}//loginChk
	
	public MemberInfoDomain searchMemmberInfo(String id) {
		MemberInfoDomain miDomain=null;
		TextEncryptor te = Encryptors.text(key, salt);
		try {
			miDomain=mmDAO.selectMemberInfo(id);
			String email=miDomain.getEmail();
			String tel =miDomain.getTel();
			if(!"".equals(tel)&&tel!=null) {
				
				miDomain.setTel(te.decrypt(tel));
			}//end if
			if(!"".equals(email)&&email!=null) {
				
				miDomain.setEmail(te.decrypt(email));
			}//end if
			System.out.println("비밀번호----"+miDomain.getPass());
			
			System.out.println(miDomain.getEmailDomain()+"///"+miDomain.getEmailIdStr());
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		
		}//end catch
		
		return miDomain;
	}//searchMemmberInfo
	
	public boolean modifyMemberInfo(MemberDTO mDTO) {
		
		boolean flag=false;
		TextEncryptor te = Encryptors.text(key, salt);
		
		try {
			mDTO.setMemberEmail(te.encrypt(mDTO.getMemberEmail()));
			mDTO.setMemberTel(te.encrypt(mDTO.getMemberTel()));
			flag=mmDAO.updateMemberModify(mDTO)==1;
			
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		}//end catch
		
		return flag;
	}//modifyMemberInfo
	
	
	
	public boolean modifyPassword(PasswordCheckDTO pcDTO) {
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		boolean flag =false;
		
		try {
			
			String DBPass= mmDAO.selectPasswordCheck(pcDTO);
			String nowPass=pcDTO.getCurrentPassword();
				
			flag=bpe.matches(nowPass, DBPass);
			
			if(flag) {
				pcDTO.setNewPassword(bpe.encode(pcDTO.getNewPassword()));
				mmDAO.updatePassword(pcDTO);
			}//end if
			
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		}//end catch
		
		return flag; //맞으면 true 틀리면 false
		
		
	}//modifyPassword
	
	public boolean removeMember(PasswordCheckDTO pcDTO) {
		boolean flag=false;
		
		try {
			flag=mmDAO.removeMember(pcDTO);
			
			
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		}//end catch
		
		
		return flag;
		
		
	}//removeMember
	
	
}//class
