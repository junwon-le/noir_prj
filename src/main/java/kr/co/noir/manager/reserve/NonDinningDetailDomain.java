package kr.co.noir.manager.reserve;

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
		dinningVisitDate;
}
