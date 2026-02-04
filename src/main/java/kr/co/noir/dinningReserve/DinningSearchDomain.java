package kr.co.noir.dinningReserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.ToString;

@Alias("dinningSearchDomain")

@Getter
@ToString
public class DinningSearchDomain {
	private int totalTable , usedTable, remainingTable;
	private String dinningTime;
}
