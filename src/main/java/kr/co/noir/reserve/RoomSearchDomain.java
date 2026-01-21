package kr.co.noir.reserve;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RoomSearchDomain {
	private String room_type, room_detail, room_img1 ; 
	private int room_price ,room_max_person,room_type_num;
}
