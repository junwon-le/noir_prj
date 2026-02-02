package kr.co.noir.mypageInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PasswordDTO {

	String memberId,currentPassword,newPassword,ConfirmPassword; 
}//class
