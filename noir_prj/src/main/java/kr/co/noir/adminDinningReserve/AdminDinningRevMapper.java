package kr.co.noir.adminDinningReserve;

import java.security.BasicPermission;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import kr.co.noir.mypageReserve.DinningRevDetailDomain;

@Mapper
public interface AdminDinningRevMapper {

	
	
	public int totalCount(AdminRangeDTO arDTO) throws PersistenceException;
	
	
	//다이닝에약 회원리스트 출력하는 Query
	/*	<select id="selectAdminDinningRevList" parameterType="arDTO" resultType="adrdDomain">*/
	public List<AdminDinningRevDomain> selectAdminDinningRevList(AdminRangeDTO arDTO) throws PersistenceException;
	
	

	//<select id="selectOneAdminDinningDetail" parameterType="int" resultType="dinningRevDetailDomain">
	public DinningRevDetailDomain selectOneAdminDinningDetail(int reserveNum) throws PersistenceException;
	
	
	public int removeDinningReserve(int reserveNum) throws PersistenceException;
	
	public int removeRevPay(int reserveNum) throws PersistenceException;
	
	
	
}//class