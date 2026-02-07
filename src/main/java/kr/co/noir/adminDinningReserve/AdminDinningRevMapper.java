package kr.co.noir.adminDinningReserve;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

@Mapper
public interface AdminDinningRevMapper {

	
	
	public int totalCount(AdminRangeDTO arDTO) throws PersistenceException;
	
	
	//다이닝에약 회원리스트 출력하는 Query
	/*	<select id="selectAdminDinningRevList" parameterType="arDTO" resultType="adrdDomain">*/
	public List<AdminDinningRevDomain> selectAdminDinningRevList(AdminRangeDTO arDTO) throws PersistenceException;
	
	
}//class
