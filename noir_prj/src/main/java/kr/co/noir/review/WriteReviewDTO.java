package kr.co.noir.review;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WriteReviewDTO {
	
	private int reviewNum,memberNum,reviewStar,roomTypeNum,roomResNum;
	private String reviewTitle,reviewMsg,roomType,reviewImg1;
	
	private List<MultipartFile> photos;

	
	
}
