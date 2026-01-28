package kr.co.noir.mypageInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PasswordCheckDTO {

	private String memberid;
	private String currentPassword;
	private String newPassword,ConfirmPassword; 
}
