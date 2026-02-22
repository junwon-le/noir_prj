package kr.co.noir.nonMemberReserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("nonMemberRevDTO")
@Getter
@Setter
@ToString
public class NonMemberRevDTO {

	private String reserveType;
	private String reserveNum;
	private String email;
	private String password;
	
}//class
