package kr.co.noir.event;

import java.sql.Date;

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
public class EventAdminDomain {
	
	  private int eventNum, adminNum;
	  private Date eventStartDate, eventDate, eventEndDate ;
	  private String eventTitle, eventSubTitle, eventImg1, eventImg2, eventMsg;

}
