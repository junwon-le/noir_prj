package kr.co.noir.mypageReserve;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReserveSearchDomain {
	
	 private int reserveNum,reservePerson;
	 private Date reserveDate ;
	 private String reserveFlag; 	
}
