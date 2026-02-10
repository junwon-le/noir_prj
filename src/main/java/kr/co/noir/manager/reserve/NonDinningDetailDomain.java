package kr.co.noir.manager.reserve;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Alias("nonDinningDetailDomain")

@Setter
@Getter
@ToString
public class NonDinningDetailDomain extends NonAdminDetailDomain {
	private String 
		dinningResDetailTime,
		dinningResType,    
		
		reserveNum,
		reserveTime,
		cardNum
		,payAgency;
	private Date dinningVisitDate;
}
