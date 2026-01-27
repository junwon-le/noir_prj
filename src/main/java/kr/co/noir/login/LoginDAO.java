package kr.co.noir.login;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository
public class LoginDAO {

	public LoginMemberDomain selectOneMember(String memberId) throws PersistenceException {
		LoginMemberDomain md=null;
		
		SqlSession ss=MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		md=ss.selectOne("kr.co.noir.login.MemberMapper.selectOneUserInfo", memberId);
		if( ss!= null) {ss.close(); } //end if
		
		return md;
		
	}//selecOneMember

	
}//class
