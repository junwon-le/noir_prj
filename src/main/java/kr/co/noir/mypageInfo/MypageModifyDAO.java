package kr.co.noir.mypageInfo;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;
import kr.co.noir.login.MemberDTO;

@Repository
public class MypageModifyDAO {

	public String selectPasswordCheck(PasswordCheckDTO pcDTO) throws PersistenceException{
		
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		String pass=ss.selectOne("kr.co.noir.mypageInfo.passwordCheck",pcDTO.getMemberid());
		
//		System.out.println(pass);
		if(ss!=null) {ss.close();}//end if
		return pass;
	
	}//selectPasswordCheck
	
	public MemberInfoDomain selectMemberInfo(String id) throws PersistenceException {
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		MemberInfoDomain miDomain=ss.selectOne("kr.co.noir.mypageInfo.memberInfo",id);
		
		
		if(ss!=null) {ss.close();}//end if
		
		return miDomain;
		
		
		
		
	}//selectMemberInfo
	
	public int updateMemberModify(MemberDTO mDTO) throws PersistenceException{
		int cnt=0;
		
		SqlSession ss= MyBatisHandler.getInstance().getMyBatisHandler(true);
		
//		System.out.println("------------"+mDTO);
		cnt=ss.update("kr.co.noir.mypageInfo.updateMember",mDTO);
		if(ss!=null) {ss.close();}//end if
		
//		System.out.println("dao----"+cnt);
		return cnt;
	}///updateMemberModify
	
	
	public int updatePassword(PasswordCheckDTO pcDTO) {
		int cnt =0;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		
		cnt=ss.update("kr.co.noir.mypageInfo.updatePassword",pcDTO);
		
		
		
//		System.out.println("변경내역----------"+cnt);
		if(ss!=null) {ss.close();}//end if
		
		
		return cnt;
	}//updatePassword
	
	public boolean removeMember(PasswordCheckDTO pcDTO) throws PersistenceException{
		int cnt =0;

		SqlSession ss= MyBatisHandler.getInstance().getMyBatisHandler(true);
		cnt=ss.update("kr.co.noir.mypageInfo.removeMember",pcDTO);
		
		if(ss!=null) {ss.close();}//end if
		return cnt==1;
		
		
	}//removeMember
	
	
}//class

