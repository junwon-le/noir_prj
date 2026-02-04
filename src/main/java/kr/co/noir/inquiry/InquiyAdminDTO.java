package kr.co.noir.inquiry;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class InquiyAdminDTO {
	 
	private int inquiryNum, memberId;
	private String inquiryTitle, inquiryMsg, inquiryReturn, inquiryDelFlag;
	private Date inquiryDate;

}
