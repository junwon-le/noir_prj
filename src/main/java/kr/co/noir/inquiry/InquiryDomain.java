package kr.co.noir.inquiry;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InquiryDomain {
	
	private String inquiryTitle,inquiryMsg,inquiryReturn;
	private int memberNum,inquiryNum;
	private Date inquiryDate;
	
}
