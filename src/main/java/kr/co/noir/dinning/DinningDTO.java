package kr.co.noir.dinning;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class DinningDTO {
	private String dinningImg1, dinningImg2, dinningImg3, dinningImg4, dinningImg5;
	private String morningStr, morningEnd, lunchStr, lunchEnd, dinnerStr, dinnerEnd;
	private String morningMain, morningSub, lunchMain, lunchSub, DinnerMain, DinnerSub;
	private String dinningEx, dinningDetail;
	private int morningAdult, morningKid, lunchAdult, lunchKid, dinnerAdult, dinnerKid;
	
	
	
}
