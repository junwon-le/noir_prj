package kr.co.noir.room;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class RoomDomain {
	private String roomType, roomBadType, roomCheckIn, roomCheckOut, roomArea, roomDetail, roomTodayPrice, roomSelectPrice;
	private int roomMaxPerson;
	private String[] roomImg = new String[5];
	private String[] roomAmenity = new String[5];
	private String[] roomService = new String[10];
	
	
}
