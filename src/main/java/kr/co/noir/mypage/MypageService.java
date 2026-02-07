package kr.co.noir.mypage;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.noir.login.MemberDTO;

@Service
public class MypageService {

	@Autowired
	MypageDAO md;
	
	public String searchMemberName(String id) {
		
		String memberName ="";
		
		
		try {
			memberName=md.selectMemberName(id);
			
			
		}catch (PersistenceException pe) {

			pe.printStackTrace();
		}//end catch
		
		
		return memberName ;
		
		
	}//searchMemberName
	
	public int searchHotelRevCnt(String id) {
		int cnt = 0;
		
		try {
			cnt =md.selectHotelRevCnt(id);
		}catch(PersistenceException pe){
			pe.printStackTrace();
		}//end catch

		return cnt;
		
	}//searchHotelRevCnt
	
	
	public int searchDinningRevCnt(String id) {
		
		int cnt =0;
		
		try {
			cnt=md.selectDinningRevCnt(id);
//			System.out.println("-----service"+cnt);
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		}//end catch
		
		
		return cnt;
	}

	public List<EventDomain> searchEventList(){
		List<EventDomain> list = new ArrayList<EventDomain>();
		
		try {
			list=md.selectEventList();
			
			
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		}//end catch
		
		return list;
		
	}//searchEventList
	
	
	public boolean loginChk(MemberDTO mDTO) {
		boolean flag = false;
		
		if(mDTO!=null && mDTO.getMemberId()!=null) {
			flag=true;
			
		}//end if
		
		
		return flag;
		
		
	}
	
}//class
