package kr.co.noir.adminHotelRevReserve;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import kr.co.noir.adminDinningReserve.AdminRangeDTO;

@Mapper
public interface AdminHotelRevMapper {

	//<select id="totalCount" resultType="int">
	public int totalCount(AdminRangeDTO arDTO) throws PersistenceException;
	
	//<select id="adminHotelRevList" parameterType="adminRangeDTO" resultType="adminHotelSearchDomain">
	public List<AdminHotelRevSearchDomain> adminHotelRevList(AdminRangeDTO adDTO) throws PersistenceException;
	
	
	//<select id="adminHotelRevDetail" parameterType="int" resultType="adminHotelRevDetailDomain">
	public List<AdminHotelRevDetailDomain> adminHotelRevDetail(int reserveNum) throws PersistenceException; 
	
	
	public int removeHotelReserve(int reserveNum) throws PersistenceException;
	
	public int removeRevPay(int reserveNum) throws PersistenceException;
	
	
}//class
