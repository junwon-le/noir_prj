package kr.co.noir.inquiry;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class InquiyAdminDomain {
	 
	private int inquiryNum, memberId, adminNum;
	private String inquiryTitle, inquiryMsg, inquiryReturn, memberEmail;
	private Date inquiryDate;

}
