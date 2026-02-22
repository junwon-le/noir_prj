package kr.co.noir.dinningReserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("dinningDependingDTO")

@Getter
@Setter
@ToString
public class DinningDependingDTO {
	private String sessionId, dependingTime,	reserveDate,	reserveTime;	
	private int inningMenuNum=1,pendingPerson;	

}
