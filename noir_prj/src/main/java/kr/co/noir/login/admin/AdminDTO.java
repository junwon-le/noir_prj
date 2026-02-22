package kr.co.noir.login.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminDTO {
	private Integer adminNum;      // admin_num
	private String adminId;    // admin_id
	private String adminPass;  // admin_pass
	private String ip;
	private String result;
}