package kr.co.noir.adminHotelRevReserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Alias("adminHotelSearchDomain")
@ToString
@Setter
@Getter
public class AdminHotelRevSearchDomain {
	
	private int reserveNum,totalPrice;
	private String reserveName,memberId,reserveTime,startDate,endDate,inputDate,reserveFlag;
	
	
	
	
}//class
