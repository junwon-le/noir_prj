package kr.co.noir.mypageReserve;

import java.sql.Date;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class ReserveDetailDomain {

	/*같은거
	 * reserveNum,reserveName,reserveDate,reserveEmail,reserveTel,adultCnt,kidCnt
	 * payAgency,cardNum,payInputDate,reserveStatus,reserveMsg
	 */
	
	/*다른거
	 * roomType,startDate,endDate,checkInTime,checkOutTime
	 * 
	 * 
	 * */
	
	
	private int reserveNum,adultCount,childCount,cancelFee,
	roomPrice,roomTax,roomPriceTotal;
	private String reserveName,email,tel,reserveMsg,reserveFlag,
	payAgency,cardData,reserveDate;
	private String payInputDate;
	
	public int getRoomPrice() {
		roomPrice = (int)(roomPriceTotal / 1.1);
	    return roomPrice;
	}
	public int getRoomTax() {
		roomTax = roomPriceTotal - (int)(roomPriceTotal / 1.1);
	    return roomTax;
	}
	public int getRoomPriceTotal() {
		return roomPriceTotal;
	}
	public int getReserveNum() {
		return reserveNum;
	}
	public int getAdultCount() {
		return adultCount;
	}
	public int getChildCount() {
		return childCount;
	}
	public int getCancelFee() {
		return cancelFee;
	}
	public String getReserveName() {
		return reserveName;
	}
	public String getEmail() {
		return email;
	}
	public String getTel() {
		return tel;
	}
	public String getReserveMsg() {
		return reserveMsg;
	}
	public String getReserveFlag() {
		return reserveFlag;
	}
	public String getPayAgency() {
		return payAgency;
	}
	public String getCardData() {
		return cardData;
	}
	public String getPayInputDate() {
		return payInputDate;
	}
	public String getReserveDate() {
		return reserveDate;
	}
}
