package kr.co.noir.dinningReserve;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.noir.reserve.MemberDomain;
import kr.co.noir.reserve.PayInfoDTO;
import kr.co.noir.reserve.RoomReserveService;

@Service
public class DinningReserveService {

	@Autowired(required = false)
	private DinningReserveMapper drm;
	
	@Autowired
	private RoomReserveService rrs;
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
	
	public void removeDepending(String id) {
		drm.deleteDepending(id);
	}
	
	public boolean addDinningReserve(DinningReserveDTO drDTO ,PayInfoDTO pDTO) {
		//예약 테이블 데이터 처리 - 서버에서 다시 db조회하여 실제 데이터를 가져옴
		MemberDomain memberDomain=rrs.searchMember(drDTO.getUser_id());
		drDTO.setUser_num(memberDomain.getMember_num());
		drDTO.setEmail(drDTO.getEmailId()+"@"+drDTO.getEmailDomain());
		boolean flag = drm.insertDinningReserve(drDTO)==1;
		
		return flag;
	}
}//class
