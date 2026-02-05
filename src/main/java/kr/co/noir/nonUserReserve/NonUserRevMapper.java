package kr.co.noir.nonUserReserve;

import org.apache.ibatis.exceptions.PersistenceException;

import kr.co.noir.mypageReserve.HotelRevDetailDomain;

public interface NonUserRevMapper {

	
	public HotelRevDetailDomain selectOneReserve (NonUserRevDTO nurDTO) throws PersistenceException;

	
}//class
