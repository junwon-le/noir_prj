package kr.co.noir.mypage;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class EventDomain {

	int eventNum;
	String eventTitle ,eventSubTitle,eventImg1,eventImg2,eventMsg;
	Date eventStartDate,eventEndDate,eventDate;


}//class
