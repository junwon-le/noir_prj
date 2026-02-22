package kr.co.noir.manager.reserve;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class NonAdminDetailDomain {
	private String resName,nonUserEmail,nonUserTel , reserveMsg;
	private int adultNum, kidNum, resPrice;
	private long period;
}
