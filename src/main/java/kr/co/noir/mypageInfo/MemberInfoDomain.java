package kr.co.noir.mypageInfo;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Setter
@ToString
public class MemberInfoDomain {

	private String lastName, firstName,email,tel,emailIdStr,emailDomain;
	private Date birth;
	public String getLastName() {
		return lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getEmail() {
		return email;
	}
	public String getTel() {
		return tel;
	}
	public String getEmailIdStr() {
		emailIdStr=email.split("@")[0];
		return emailIdStr;
	}
	public String getEmailDomain() {
		emailDomain=email.split("@")[1];
		return emailDomain;
	}
	public Date getBirth() {
		return birth;
	}

	

	
	
}//class
