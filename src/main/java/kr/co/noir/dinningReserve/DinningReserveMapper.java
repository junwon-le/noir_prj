package kr.co.noir.dinningReserve;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import kr.co.noir.reserve.PayInfoDTO;



@Mapper
public interface DinningReserveMapper {

	public List<DinningMenuDomain> selectDinning() throws PersistenceException;
	
	public List<DinningSearchDomain> selectDinningSearch(DinningSearchDTO dsDTO) throws PersistenceException;
	
	public int insertDepending(DinningDependingDTO dDTO) throws PersistenceException;
	
	public int deleteDepending(String id) throws PersistenceException;
	
	public int insertDinningReserve(DinningReserveDTO drDTO) throws PersistenceException;
	
	public int insertDinningDetail(DinningReserveDTO drDTO) throws PersistenceException;
	
	public int insertDinningPay(PayInfoDTO pDTO)throws PersistenceException;
	
	public int insertDinningPayInfo(PayInfoDTO pDTO) throws PersistenceException;
	
	public String selectDinningtype(String dinning_time) throws PersistenceException;
	
	public int insertNonMember(DinningReserveDTO drDTO) throws PersistenceException;
	
	public int insertNonDinningReserve(DinningReserveDTO drDTO) throws PersistenceException;
	
}
