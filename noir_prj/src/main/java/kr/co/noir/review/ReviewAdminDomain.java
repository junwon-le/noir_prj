package kr.co.noir.review;

import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ReviewAdminDomain {
	 
	private int reviewStar, reviewNum, memberNum, adminNum;
	
	private String roomType, reviewTitle, reviewMsg, reviewReturn, reviewDelFlag, memberLastName;
	
	private Date reviewDate;
	
	private List<String> reviewImgList;  // 이미지 여러 장
	
}
