package kr.co.noir.inquiry;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class InquiryAdminDomain {
	 

    private int inquiryNum;
    private Integer memberNum;   // 숫자 FK
    private Integer adminNum;

    private String memberId;     // 회원 아이디 (문자열)
    private String memberEmail;

    private String inquiryTitle;
    private String inquiryMsg;
    private String inquiryReturn;

    private Date inquiryDate;

}
