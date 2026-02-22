package kr.co.noir.reserve;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RoomSearchDomain {
	private String room_type, room_detail, room_img1 ; 
	private int start_date_price ,room_max_person,room_type_num,total_sum_price,reserved_count,available_count;
	private long period;
}
