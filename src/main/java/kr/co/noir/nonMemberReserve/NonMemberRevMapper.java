package kr.co.noir.nonMemberReserve;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import kr.co.noir.mypageReserve.HotelRevDetailDomain;


@Mapper
public interface NonMemberRevMapper {

	/*	<select id="nonMemberRevCheck" parameterType="nonMemberRevDTO" resultType="String">*/
	public String nonMemberRevCheck (NonMemberRevDTO nurDTO) throws PersistenceException;
	
	
	public HotelRevDetailDomain selectOneReserve (NonMemberRevDTO nurDTO) throws PersistenceException;

	
}//class
