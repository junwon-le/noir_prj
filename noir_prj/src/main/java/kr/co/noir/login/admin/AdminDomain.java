package kr.co.noir.login.admin;

import lombok.Data;

@Data
public class AdminDomain {
	private Integer adminNum;    // MEMBER_ID
	private String adminId;    // MEMBER_ID
	private String adminPass;  // MEMBER_PASS
    private String ip;
    private String resultMsg; // 결과 메시지
}