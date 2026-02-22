package kr.co.noir.manager.reserve;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

@Mapper
public interface AdminReserveMapper {

	public int selectResTotalCnt(AdminResRangeDTO arrDTO) throws PersistenceException;
	
	public List<NonRoomResDomain> selectNonRoomList(AdminResRangeDTO arrDTO) throws PersistenceException;
	
	public List<NonRoomDetailDomain> selectnonRoomDetail(int resNum) throws PersistenceException;
	
	
	public int selectDinningTotalCnt(AdminResRangeDTO arrDTO) throws PersistenceException;

	public List<NonRoomResDomain> selectNonDinningList(AdminResRangeDTO arrDTO) throws PersistenceException;

	public NonDinningDetailDomain selectnonDinningDetail(int resNum) throws PersistenceException; 
	
	public int updateRes(int resNum) throws PersistenceException;
	public int updatePay(int resNum) throws PersistenceException;
	
}
