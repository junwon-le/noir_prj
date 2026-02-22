package kr.co.noir.adminDinningReserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Alias("adrdDomain")
@Getter
@Setter
@ToString
public class AdminDinningRevDomain {

	
	private int reserveNum,reservePerson,totalPrice;
	private String userName,memberId,dinningType,visitDate,reserveFlag,visitTime;
	
	
	
	
}//class
