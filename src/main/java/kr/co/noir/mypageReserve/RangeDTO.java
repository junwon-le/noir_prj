package kr.co.noir.mypageReserve;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RangeDTO {

	int startNum,endNum,currentPage,totalPage;
	String field,keyword,fieldStr,url;
	
}//class
