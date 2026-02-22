package kr.co.noir.review;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewDomain {
	private int memberNum,roomResNum,reviewNum,reviewStar;
	private String reviewTitle,reviewMsg,reviewReturn,roomType,reviewImg1,reviewDelFlag,memberLastName;
	private Date reserveStartDate,reviewDate;

	
	private List<String> reviewImgList = new ArrayList<>();
}
