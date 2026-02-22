package kr.co.noir.event;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventAdminDTO {
	
	  private int eventNum, adminNum;
	  private Date eventStartDate, eventDate, eventEndDate ;
	  private String eventTitle, eventSubTitle, eventImg1, eventImg2, eventMsg, eventDelFlag ;

}
