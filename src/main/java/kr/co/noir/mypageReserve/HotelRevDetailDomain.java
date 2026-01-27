package kr.co.noir.mypageReserve;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@ToString
public class HotelRevDetailDomain {
	private  int reserveNum,adultCount,kidCount,cancelFee,roomPrice,roomTax,roomPriceTotal ;
	private String reserveName, roomType,
	checkIn,checkOut,reserveDate,
	email,tel,reserveMsg,reserveFlag;
	
	
	public int getReserveNum() {
		return reserveNum;
	}
	public int getAdultCount() {
		return adultCount;
	}
	public int getKidCount() {
		return kidCount;
	}
	public int getCancelFee() {
		return cancelFee;
	}
	public String getReserveName() {
		return reserveName;
	}
	public String getRoomType() {
		return roomType;
	}
	public String getCheckIn() {
		return checkIn;
	}
	public String getCheckOut() {
		return checkOut;
	}
	public String getReserveDate() {
		return reserveDate;
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
	
	
	
	
}
