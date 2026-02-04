package kr.co.noir.dinningReserve;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DinningReserveMapper {

	public List<DinningMenuDomain> selectDinning() ;
	
	public List<DinningSearchDomain> selectDinningSearch(DinningSearchDTO dsDTO) ;
	
	public int insertDepending(DinningDependingDTO dDTO);
	
}
