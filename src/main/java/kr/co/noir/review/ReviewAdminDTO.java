package kr.co.noir.review;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ReviewAdminDTO {
	 
	private int reviewStar, reviewNum, memberNum, adminNum;
	private String reviewTitle, reviewMsg, reviewReturn, reviewDelFlag;
	private Date reviewDate;

}
