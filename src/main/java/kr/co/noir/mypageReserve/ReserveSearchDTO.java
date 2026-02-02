package kr.co.noir.mypageReserve;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReserveSearchDTO extends RangeDTO{

	private String memberId; 
	private	 String startDate,endDate;

		
}//class
