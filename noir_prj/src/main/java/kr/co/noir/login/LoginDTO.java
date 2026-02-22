package kr.co.noir.login;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginDTO {
	private String memberId, memberPass, memberLastName, memberFirstName, memberIp, result;
}
