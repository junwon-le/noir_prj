package kr.co.noir.dinningReserve;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siot.IamportRestClient.IamportClient;

import kr.co.noir.reserve.MemberDomain;
import kr.co.noir.reserve.PayInfoDTO;
import kr.co.noir.reserve.RoomReserveService;

@Service
public class DinningReserveService {

	@Autowired(required = false)
	private DinningReserveMapper drm;
	
	@Autowired
	private RoomReserveService rrs;
	
	@Autowired
	private IamportClient client;
	
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
	
	@Transactional
	public boolean addDinningReserve(DinningReserveDTO drDTO ,PayInfoDTO pDTO) {
		//예약 테이블 데이터 처리 - 서버에서 다시 db조회하여 실제 데이터를 가져옴
		MemberDomain memberDomain=rrs.searchMember(drDTO.getUser_id());
		drDTO.setUser_num(memberDomain.getMember_num());
		drDTO.setEmail(drDTO.getEmailId()+"@"+drDTO.getEmailDomain());
		drDTO.setVisite_date(LocalDate.parse(drDTO.getStr_visite_date()));
		
		//결제 정보 서버에서 가져오기
		//예약 시간에 해당하는 다이닝 타입 조회 - 서버
		String dinningType = drm.selectDinningtype(drDTO.getDinning_time());
		int adultPrice = 0;
		int kidPrice = 0;
		List<DinningMenuDomain> list = drm.selectDinning();
		//서버에서 금액 정보 가져오기
		for( DinningMenuDomain type : list) {
			if(dinningType.equals(type.getDinningMenuType())) {
				adultPrice = type.getDinningAdultPrice();
				kidPrice = type.getDinningKidPrice();
			}
		}
		int totalPrice = adultPrice*drDTO.getReserve_adult_count()+kidPrice*drDTO.getReserve_kid_count();
		pDTO.setPay_price(totalPrice);
		
		boolean flag = true; 
		//결제 정보 데이터 처리
			try {
				// 빌링키 정보를 조회
				var response = client.getBillingCustomer(pDTO.getBilling_key());
				if (response.getResponse() == null) {
		            throw new RuntimeException("결제 정보 조회 실패"); 
		        }
				pDTO.setAgency(response.getResponse().getCardName());;//카드사
				pDTO.setCard_number(response.getResponse().getCardNumber());//카드 번호 
				pDTO.setPg_provider(response.getResponse().getPgProvider());//PG사 구분 코드
		//예약 테이블 추가
		int cnt =drm.insertDinningReserve(drDTO);
		int cnt2 =drm.insertDinningDetail(drDTO);
		pDTO.setReserve_num(drDTO.getReserve_num());
		int cnt3 = drm.insertDinningPay(pDTO);
		int cnt4 = drm.insertDinningPayInfo(pDTO);
		
			} catch (Exception e) {
				e.printStackTrace();
		        throw new RuntimeException(e); // 여기서 다시 던져야 트랜잭션이 롤백됨!
			}
		return flag;
	}//addDinningReserve
	@Transactional
	public boolean addNonDinningReserve(DinningReserveDTO drDTO ,PayInfoDTO pDTO) {
		//예약 테이블 데이터 처리 - 서버에서 다시 db조회하여 실제 데이터를 가져옴
		drDTO.setEmail(drDTO.getEmailId()+"@"+drDTO.getEmailDomain());
		drDTO.setVisite_date(LocalDate.parse(drDTO.getStr_visite_date()));
		
		//결제 정보 서버에서 가져오기
		//예약 시간에 해당하는 다이닝 타입 조회 - 서버
		String dinningType = drm.selectDinningtype(drDTO.getDinning_time());
		int adultPrice = 0;
		int kidPrice = 0;
		List<DinningMenuDomain> list = drm.selectDinning();
		//서버에서 금액 정보 가져오기
		for( DinningMenuDomain type : list) {
			if(dinningType.equals(type.getDinningMenuType())) {
				adultPrice = type.getDinningAdultPrice();
				kidPrice = type.getDinningKidPrice();
			}
		}
		int totalPrice = adultPrice*drDTO.getReserve_adult_count()+kidPrice*drDTO.getReserve_kid_count();
		pDTO.setPay_price(totalPrice);
		
		boolean flag = true; 
		//결제 정보 데이터 처리
		try {
			// 빌링키 정보를 조회
			var response = client.getBillingCustomer(pDTO.getBilling_key());
			if (response.getResponse() == null) {
				throw new RuntimeException("결제 정보 조회 실패"); 
			}
			pDTO.setAgency(response.getResponse().getCardName());;//카드사
			pDTO.setCard_number(response.getResponse().getCardNumber());//카드 번호 
			pDTO.setPg_provider(response.getResponse().getPgProvider());//PG사 구분 코드
			//예약 테이블 추가
			int cnt5 = drm.insertNonMember(drDTO);
			int cnt =drm.insertNonDinningReserve(drDTO);
			int cnt2 =drm.insertDinningDetail(drDTO);
			pDTO.setReserve_num(drDTO.getReserve_num());
			int cnt3 = drm.insertDinningPay(pDTO);
			int cnt4 = drm.insertDinningPayInfo(pDTO);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e); // 여기서 다시 던져야 트랜잭션이 롤백됨!
		}
		return flag;
	}//addNonDinningReserve
}//class
