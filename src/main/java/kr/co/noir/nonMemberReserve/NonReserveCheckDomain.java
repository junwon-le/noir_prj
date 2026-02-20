package kr.co.noir.nonMemberReserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Alias("nonReserveCheckDomain")
@ToString
@Setter
@Getter
public class NonReserveCheckDomain {

	private String password,email;
}//class
