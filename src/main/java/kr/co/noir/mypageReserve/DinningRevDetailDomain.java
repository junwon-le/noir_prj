package kr.co.noir.mypageReserve;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DinningRevDetailDomain extends ReserveDetailDomain {

	private String dinningName,dinningType,visitTime;
	private Date reserveDate,visitDate;
	
	
	
	
}//class
