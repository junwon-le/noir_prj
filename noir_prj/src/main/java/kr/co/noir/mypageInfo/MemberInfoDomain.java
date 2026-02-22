package kr.co.noir.mypageInfo;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Setter
@ToString
public class MemberInfoDomain {

	private String lastName, firstName,email,tel,emailIdStr,emailDomain,pass;
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
		if(email != null && email.contains("@")) {
			emailIdStr=email.split("@")[0];
			
		}//end if
		return emailIdStr;
	}
	public String getEmailDomain() {
		if(email != null && email.contains("@")){
			emailDomain=email.split("@")[1];
		}//end if
		return emailDomain;
	}
	public Date getBirth() {
		return birth;
	}
	public String getPass() {
		return pass;
	}

	

	
	
}//class
