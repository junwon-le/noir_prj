package kr.co.noir.adminDinningReserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Alias("adminRangeDTO")
@Getter
@Setter
@ToString
public class AdminRangeDTO {

	private int startNum,endNum;
	private int currentPage=1;
	private int totalPage=0;
	private String field,keyword,fieldStr,url;
	private String reserveType;
	

}//class
