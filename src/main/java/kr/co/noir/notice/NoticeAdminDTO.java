 package kr.co.noir.notice;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
 
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString

public class NoticeAdminDTO {
	
		private int noticeNum, memberId, adminNum; 
		private String noticeTitle,noticeMsg,noticeCategory, ip;
		private Date noticeDate;

			
	}

