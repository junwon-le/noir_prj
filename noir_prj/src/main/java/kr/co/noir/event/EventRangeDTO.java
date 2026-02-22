package kr.co.noir.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter 
@AllArgsConstructor
@NoArgsConstructor
public class EventRangeDTO {
	
	private int startNum, endNum;//시작번호, 끝번호
	private String field , keyword;//검색 필드 1,2,3, 검색값
	private String fieldStr ;//검색필드값에 대응되는 컬럼명의 문자열
	
	private String url;
	private int currentPage=1;//이동할 URL, 현재 페이지
	private int totalPage=0;//총 페이지

	
	public String getFieldStr() {
		String[] fieldTitle= {"title","content","id"};
		int tempField=Integer.parseInt(field);
		if(!(tempField>0 && tempField<4)) {//1~3사이가 아닌 경우
			tempField=1;
		}//enf if
		fieldStr=fieldTitle[tempField-1];
		return fieldStr;
	}//getFieldStr

	
	
	
}//class
