package kr.co.noir.reserve;



import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;


@Mapper
public interface RoomReserveMapper {

	public List<RoomSearchDomain1> selectRoom1(RoomSearchDTO1 drDTO1) throws PersistenceException;
	
}
