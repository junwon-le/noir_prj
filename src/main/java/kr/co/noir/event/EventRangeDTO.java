package kr.co.noir.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventRangeDTO {
	private int currentPage;  
    private int totalPage;    
    private int startNum;     
    private int endNum;
    
    private String keyword;  
    private String category;   
}
