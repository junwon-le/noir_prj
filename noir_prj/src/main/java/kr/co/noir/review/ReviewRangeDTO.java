package kr.co.noir.review;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReviewRangeDTO {
	private int startNum,endNum;//시작 번호, 끝 번호 
	private String field, keyword; //검색필드 1,2,3, 검색값
	private String category;//검색필드값에 대응되는 컬렴명의 문자열
	private String url;//이동할 url
	private String ip;//작성자 ip
	private int currentPage=1;//이동할 url, 현재 페이지
	private int totalPage=0; //총 페이지
	private int memberNum;
	private int roomTypeNum;

	



}

