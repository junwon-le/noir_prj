package kr.co.noir.adminHotelRevReserve;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Alias("adminHotelRevDetailDomain")
@ToString
@Setter
@Getter
public class AdminHotelRevDetailDomain {
	private int reserveNum,totalPrice,adultCount,kidCount,roomPrice,roomTax,stayCount;
	private String reserveName,memberId,startDate,endDate,payInfoInputDate,payAgency,
	email,tel,reserveMsg,roomImg,reserveFlag,checkIn,checkOut,cardData,roomType;
	
	
	public int getRoomPrice() {
	    // totalPrice * 0.9는 roomPrice 계산에 사용
	    roomPrice = (int) (totalPrice * 0.9);
	    return roomPrice;
	}

	public int getRoomTax() {
	    // totalPrice * 0.1은 roomTax 계산에 사용
	    roomTax = (int) (totalPrice * 0.1);
	    return roomTax;
	}

}//class

