package kr.co.noir.inquiry;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class InquiryAdminDTO {
	 
    private int inquiryNum;
    private Integer memberNum;

    private String inquiryTitle;
    private String inquiryMsg;
    private String inquiryReturn;
    private String inquiryDelFlag;

    private Date inquiryDate;

}
