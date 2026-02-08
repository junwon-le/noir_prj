package kr.co.noir.manager.reserve;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminReserveMapper {

	public int selectResTotalCnt(AdminResRangeDTO arrDTO);
	
	public List<NonRoomResDomain> selectNonRoomList(AdminResRangeDTO arrDTO);
	
	public NonRoomDetailDomain selectnonRoomDetail(int resNum);
}
