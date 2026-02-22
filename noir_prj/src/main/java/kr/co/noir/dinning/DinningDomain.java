package kr.co.noir.dinning;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class DinningDomain {
	private String dinningImg1,dinningImg2,dinningImg3,dinningImg4,dinningImg5;
	private String dinningName, dinningEx, dinningDetail, dinningTime;
	private int dinningMaxTable, adultPrice, kidPrice;
	private String dinningMorningStr, dinningMorningEnd;
	private String dinningLunchStr, dinningLunchEnd;
	private String dinningDinnerStr, dinningDinnerEnd;
	private String dinningMenuType, dinningMenuMain, dinningMenuSub;
}
