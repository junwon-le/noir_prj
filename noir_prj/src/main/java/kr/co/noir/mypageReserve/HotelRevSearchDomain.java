package kr.co.noir.mypageReserve;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true)
public class HotelRevSearchDomain extends ReserveSearchDomain {
	
	 private String roomType ;
	 private Date checkIn,checkOut;
	
	
}//class
