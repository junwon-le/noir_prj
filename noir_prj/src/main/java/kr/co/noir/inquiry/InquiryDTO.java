package kr.co.noir.inquiry;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InquiryDTO {
	
	private String inquiryTitle,inquiryMsg,inquiryReturn,memberLastName,memberFirstName,
	memberTel,memberEmail;
	private int memberNum;
	private Date inquiryDate;
	
}
