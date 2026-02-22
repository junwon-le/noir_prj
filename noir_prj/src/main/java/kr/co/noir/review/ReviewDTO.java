package kr.co.noir.review;

import java.sql.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewDTO {
	private int memberNum,roomResNum,reviewNum,reviewStar;
	private String reviewTitle,reviewMsg,reviewReturn,roomType,reviewImg1,reviewDelFlag,memberLastName;
	private Date reserveStartDate,reviewDate;

	private List<MultipartFile> photos;
	private List<String> reviewImgList;
	
	
}
