package kr.co.noir.inquiry;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InquiryDTO {
	
	private String inquiryTitle,inquiryMsg,inquiryReturn;
	private Date inquiryDate;
	
}
