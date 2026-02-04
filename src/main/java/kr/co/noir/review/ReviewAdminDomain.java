package kr.co.noir.review;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ReviewAdminDomain {
	 
	private int reviewStar, reviewNum, memberId;
	private String roomType, reviewTitle, reviewMsg, reviewReturn, reviewDelFlag;
	private Date reviewDate;

}
