package kr.co.noir.nonUserReserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("NonUserDTO")
@Getter
@Setter
@ToString
public class NonUserRevDTO {

	private String reserveType;
	private String reserveNum;
	private String email;
	private String password;
	
}//class
