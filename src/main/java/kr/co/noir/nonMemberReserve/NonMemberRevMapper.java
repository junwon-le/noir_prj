package kr.co.noir.nonMemberReserve;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.exceptions.PersistenceException;

import kr.co.noir.mypageReserve.DinningRevDetailDomain;
import kr.co.noir.mypageReserve.HotelRevDetailDomain;


@Mapper
public interface NonMemberRevMapper {

	//예약조회시 내역확인을 위한 예약번호, 이메일, 비밀번호 체크
	/*<select id="nonMemberRevCheck" parameterType="nonMemberRevDTO" resultType="String">*/
	public NonReserveCheckDomain nonMemberRevCheck (NonMemberRevDTO nurDTO) throws PersistenceException;
	
	
	//예약체크 후 있으면 보여줄 Hoteldetail
	/*<select id="selectOneHotelDetail" parameterType="nonMemberRevDTO" resultMap="hrdDomain">	*/
	public List<HotelRevDetailDomain> selectOneHotelDetail (NonMemberRevDTO nurDTO) throws PersistenceException;
	
	
	public DinningRevDetailDomain selectOnedinningDetail (NonMemberRevDTO nurDTO) throws PersistenceException;

	
}//class
