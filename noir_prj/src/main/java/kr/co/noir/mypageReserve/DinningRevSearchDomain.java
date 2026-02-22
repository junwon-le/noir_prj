package kr.co.noir.mypageReserve;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(callSuper = true)
public class DinningRevSearchDomain extends ReserveSearchDomain {
	
	private Date visitDate;
	private String dinningType;
	private String visitTime;
}
