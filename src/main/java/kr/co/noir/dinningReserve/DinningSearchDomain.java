package kr.co.noir.dinningReserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("dinningSearchDomain")

@Setter
@Getter
@ToString
public class DinningSearchDomain {
	
	private String dinningMenuType,	dinningMenuMain,	dinningMenuSub, dinningAdultPrice, dinningKidPrice;	

}
