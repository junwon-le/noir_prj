package kr.co.noir.mypageReserve;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReserveSearchDTO extends RangeDTO{

	 int memberNum; 
	 Date startDate,endDate;
	
}//class
