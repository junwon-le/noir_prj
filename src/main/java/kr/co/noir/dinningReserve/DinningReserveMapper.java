package kr.co.noir.dinningReserve;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface DinningReserveMapper {

	public List<DinningMenuDomain> selectDinning() ;
	
	public List<DinningSearchDomain> selectDinningSearch(DinningSearchDTO dsDTO) ;
	
	public int insertDepending(DinningDependingDTO dDTO);
	
	public int deleteDepending(String id);
	
	public int insertDinningReserve(DinningReserveDTO drDTO);
	
	public int insertDinningDetail(DinningReserveDTO drDTO);
	
	public int insertDinningPay(PayInfoDTO pDTO);
	
	public int insertDinningPayInfo(PayInfoDTO pDTO);
	
	public String selectDinningtype(String dinning_time);
	
	public int insertNonMember(DinningReserveDTO drDTO);
	
	public int insertNonDinningReserve(DinningReserveDTO drDTO);
	
}
