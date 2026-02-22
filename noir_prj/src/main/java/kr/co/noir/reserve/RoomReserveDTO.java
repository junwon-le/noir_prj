package kr.co.noir.reserve;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RoomReserveDTO {
	private String user_last_name,user_first_name,reserve_msg,
	user_id,reserve_type,reserve_ip,emailId,emailDomain,email,
	reserve_tel,non_user_pass,startDate,endDate;
	private int reserve_adult_count, reserve_kid_count,user_num ,reserve_num, temp_room_type, non_user_num;
	private List<Integer> room_type ;
	private LocalDate room_res_startDate,room_res_endDate;	

}
