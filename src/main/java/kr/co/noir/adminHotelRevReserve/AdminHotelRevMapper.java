package kr.co.noir.adminHotelRevReserve;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import kr.co.noir.adminDinningReserve.AdminRangeDTO;

@Mapper
public interface AdminHotelRevMapper {

	public int totalCount(AdminRangeDTO arDTO) throws PersistenceException;
	
	
	public List<AdminHotelRevSearchDomain> adminHotelRevList(AdminRangeDTO adDTO) throws PersistenceException;
	
	
	
}//class
