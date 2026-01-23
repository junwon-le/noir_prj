package kr.co.noir.mypageInfo;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository
public class MypageModifyDAO {

	public boolean selectPasswordCheck(PasswordCheckDTO pcDTO) throws PersistenceException{
		
		boolean flag = false;
		
		String password=pcDTO.getCurrentPassword();
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(flag);
		
		String pass=ss.selectOne("kr.co.noir.mypageInfo.passwordCheck",pcDTO.getMemberid());
		
		flag=pass.equals(password);
		
		System.out.println(flag);
		
		return flag;
		
		
		 
		
	}//selectPasswordCheck
	
	
	
}//class

