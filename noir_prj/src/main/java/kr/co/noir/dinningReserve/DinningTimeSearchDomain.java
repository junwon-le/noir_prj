package kr.co.noir.dinningReserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.ToString;


@Alias("dinningTimeSearchDomain")
@Getter
@ToString
public class DinningTimeSearchDomain {

	private String dinningType,	dinningDetailTime;	

}
