package kr.co.noir.dinningReserve;

import java.sql.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("payInfoDTO")

@Setter
@Getter
@ToString
public class PayInfoDTO {
	private String billing_key,	user_id ,agency, pg_provider,card_number,merchant_uid; 
	private int pay_price, reserve_num,pay_num; 
	private Date input_date; 
}
