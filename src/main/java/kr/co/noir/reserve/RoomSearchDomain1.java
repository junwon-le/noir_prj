package kr.co.noir.reserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Alias("roomSearchDomain1")
@Setter
@Getter
@ToString
public class RoomSearchDomain1 {
	private String roomType, roomDetail, roomImg1 ; 
	private int startDatePrice ,roomMaxPerson,roomTypeNum,totalSumPrice,reservedCount,availableCount;
	private long period;
}
