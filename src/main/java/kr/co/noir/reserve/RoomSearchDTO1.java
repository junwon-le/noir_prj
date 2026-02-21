package kr.co.noir.reserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("roomSearchDTO1")
@Setter
@Getter
@ToString
public class RoomSearchDTO1 {

	private String startDate, endDate;
}
