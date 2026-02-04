package kr.co.noir.dinningReserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class dinningReserveService {

	@Autowired(required = false)
	private DinningReserveMapper drm;
	
	public List<DinningSearchDomain> SearchDinning(){
		
		List<DinningSearchDomain> list = drm.selectDinning();
		
		return list;
	}//SearchDinning
	
	public List<DinningTimeSearchDomain> SearchDinningTime(String type){
		
		List<DinningTimeSearchDomain> list = drm.selectDinningTime(type);
		
		return list;
	}//SearchDinning
}//class
