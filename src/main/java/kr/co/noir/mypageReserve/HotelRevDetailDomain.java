package kr.co.noir.mypageReserve;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class HotelRevDetailDomain extends ReserveDetailDomain {
	private String roomType,checkInTime,checkOutTime;
	private Date startDate,endDate;
	
}//class
