package kr.co.noir.room;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class RoomPriceDTO {
	private String roomPriceDate;
	private int roomPrice,roomTypeNum;
}
