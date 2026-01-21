package kr.co.noir.mypage;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MypageService {

	@Autowired
	MypageDAO md;
	
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
		
		return 0;
	}

	public List<EventDomain> searchEventList(){
		List<EventDomain> list = new ArrayList<EventDomain>();
		
		return list;
		
	}
	
}//class
