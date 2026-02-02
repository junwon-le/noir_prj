package kr.co.noir.mypageReserve;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReserveSearchDTO extends RangeDTO{

	 String memberId; 
	 String startDate,endDate;
	
}//class
