package kr.co.noir.mypageInfo;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.noir.login.MemberDTO;

@Service
public class MypageModifySevice {

	@Autowired(required = false)
	MypageModifyDAO mmDAO;
	
	
	/**
	 * 회원정보수정하기전 비밀번호 확인
	 * @param pcDTO
	 * @return
	 */
	public boolean searchPasswordCheck(PasswordCheckDTO pcDTO) {
		
		boolean flag = false;
		
		try {
			
			flag=mmDAO.selectPasswordCheck(pcDTO);
			
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		}//end catch
		
		
		return flag;
		
	}//searchPasswordCheck
	
	
	
	public boolean loginChk(MemberDTO mDTO) {
		boolean flag = false;
		
		if(mDTO!=null && mDTO.getMemberId()!=null) {
			flag=true;
			
		}//end if
		return flag;
		
	}//loginChk
}//class
