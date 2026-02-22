package kr.co.noir.login;

import lombok.Data;

@Data
public class LoginMemberDomain {
	private String memberNum;    // MEMBER_Num
	private String memberId;    // MEMBER_ID
	private String memberPass;  // MEMBER_PASS
	private String memberLastName;  // MEMBER_NAME
	private String memberFirstName;  // MEMBER_NAME
	private String memberEmail; // MEMBER_EMAIL
    private String memberTel;
    private String memberBirth;
    private String memberDelFlag;
    private String memberInputDate;
    private String memberIp;
    private String resultMsg; // 결과 메시지
}