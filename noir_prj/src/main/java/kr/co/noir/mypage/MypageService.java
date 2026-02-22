package kr.co.noir.mypage;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.noir.event.EventDomain;
import kr.co.noir.login.MemberDTO;



@Service
public class MypageService {

	@Autowired
	MypageDAO md;
	
	@Autowired
    private MypageMapper mypageMapper;
	
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
	
	// 1. 회원의 암호화된 비밀번호 조회
	public String searchMemberPassword(String memberId) {
	    return mypageMapper.selectPasswordById(memberId);
	}

	// 2. 회원 탈퇴 처리 (DEL_FLAG 업데이트 등)
	public void withdrawMember(String memberId, String provider) {
	    mypageMapper.updateMemberDelFlag(memberId);
	    mypageMapper.deleteSnsToken(memberId, provider);
	    
	}
	
}//class
