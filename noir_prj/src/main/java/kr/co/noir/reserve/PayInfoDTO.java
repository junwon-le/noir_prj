package kr.co.noir.reserve;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class PayInfoDTO {
	private String billing_key,	user_id ,agency, pg_provider,card_number,merchant_uid; 
	private int pay_price, reserve_num,pay_num; 
	private Date input_date; 
}
