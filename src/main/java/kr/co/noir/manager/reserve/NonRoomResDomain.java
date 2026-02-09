package kr.co.noir.manager.reserve;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class NonRoomResDomain {
	private int reserveNum, payPrice, roomTypeNum;
	private String reserveEmail, resName, reserveStatus, reserveTime;
}


