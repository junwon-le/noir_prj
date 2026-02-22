package kr.co.noir.login.admin;

import lombok.Data;

@Data
public class AdminDTO {
	private int adminNum;      // admin_num
	private String adminId;    // admin_id
	private String adminPass;  // admin_pass
	private String ip;
	private String result;
}