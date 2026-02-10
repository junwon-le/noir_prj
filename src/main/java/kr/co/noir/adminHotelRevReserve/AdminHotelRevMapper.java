package kr.co.noir.adminHotelRevReserve;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import kr.co.noir.adminDinningReserve.AdminRangeDTO;

@Mapper
public interface AdminHotelRevMapper {

	public AdminHotelRevSearchDomain adminHotelRevList(AdminRangeDTO adDTO) throws PersistenceException;
	
}//class
