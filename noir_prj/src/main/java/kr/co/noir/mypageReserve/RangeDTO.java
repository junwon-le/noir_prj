package kr.co.noir.mypageReserve;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RangeDTO {

	private int startNum,endNum;
	private int currentPage=1;
	private int totalPage=0;
	private String field,keyword,fieldStr,url;
	private String reserveType;
}//class
