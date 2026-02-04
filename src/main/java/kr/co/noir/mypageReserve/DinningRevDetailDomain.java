package kr.co.noir.mypageReserve;

import java.sql.Date;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class DinningRevDetailDomain extends ReserveDetailDomain {

	private String dinningName,dinningType,visitTime;
	private Date visitDate;
	
	 public String getCancelFee() { 
		 if(getReserveFlag().equals("N")) { 
			 LocalDate today=LocalDate.now(); 
		 	LocalDate checkIn = ((java.sql.Date) visitDate).toLocalDate();
		 	if(today.isEqual(checkIn.minusDays(1))) {
		 		return "50% fee applies";
		 	}else if (today.isEqual(checkIn) || today.isAfter(checkIn)) {
		 		return "Non-refundable";
		 	}
		 	
		 }//end if
		  return "Free cancellation";
	 }//getCancelFee
	 
		public boolean isCheckInToday() {
			if (visitDate == null) return false;

		    // java.sql.Date는 바로 toLocalDate() 사용 가능
		    LocalDate checkIn = visitDate.toLocalDate();
		    LocalDate today = LocalDate.now();

		    // 오늘이거나 오늘보다 이전이면 true (즉, 취소 불가능한 상태)
		    return !checkIn.isAfter(today);
		}//isCheckInToday
	
	
	
	
}//class
