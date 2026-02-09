package kr.co.noir.manager.reserve;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class NonRoomResDomain {
	private int reserveNum, payPrice, roomTypeNum ,reserveAdultCount,reserveKidCount;
	private String reserveEmail, resName, reserveStatus, reserveTime; 
	private Date dinningVisitDate;
}


