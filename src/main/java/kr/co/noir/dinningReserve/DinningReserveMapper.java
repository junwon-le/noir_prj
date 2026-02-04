package kr.co.noir.dinningReserve;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DinningReserveMapper {

	public List<DinningSearchDomain> selectDinning() ;
	
	public List<DinningTimeSearchDomain> selectDinningTime(String type) ;
	
}
