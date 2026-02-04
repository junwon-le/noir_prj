package kr.co.noir.dinningReserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DinningReserveService {

	@Autowired(required = false)
	private DinningReserveMapper drm;
	
	public List<DinningMenuDomain> SearchDinning(){
		
		List<DinningMenuDomain> list = drm.selectDinning();
		
		return list;
	}//SearchDinning
	
	public List<DinningSearchDomain> SearchDinningTime(DinningSearchDTO dsDTO){
		
		List<DinningSearchDomain> list = drm.selectDinningSearch(dsDTO);
		
		return list;
	}//SearchDinningTime
	
	public boolean addDepending(DinningDependingDTO dDTO){
		
		boolean flag = drm.insertDepending(dDTO)==1;
		return flag;
	}//SearchDinning
}//class
