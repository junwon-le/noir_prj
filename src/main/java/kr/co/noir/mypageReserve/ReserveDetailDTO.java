package kr.co.noir.mypageReserve;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString 
public class ReserveDetailDTO {
	private String memberId;
	private int reserveNum;
	private String reserveType;
	private String pageStartDate,pageEndDate;
	private String startDate,endDate;
	private String period;
	private int currentpage;
	private String monthBtnValue;
	
	
}//class
