package kr.co.noir.manager.reserve;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("nonRoomDetailDomain")

@Setter
@Getter
@ToString
public class NonRoomDetailDomain extends NonAdminDetailDomain {
	private String roomDetail,
	    roomType,
		roomArea,
		roomBadType,    
	    reserveStartDate,
	    reserveEndDate,
	    roomCheckOut,
	    roomCheckIn  ,
	    roomImg1,
	    cardNum,
	    payAgency;
	    private Date reserveTime;
	private int reserveNum;
}
