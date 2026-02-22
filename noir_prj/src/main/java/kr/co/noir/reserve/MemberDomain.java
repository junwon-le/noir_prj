package kr.co.noir.reserve;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDomain {
	
	private int member_num, num;
	private String 	member_id, member_last_name, member_pass, member_first_name, member_email, member_tel,  member_del_flag, 
					 member_ip, member_provider, member_provider_id, member_email_id , member_email_domain ; 
	private Date member_birth, member_inputdate;
	
}
