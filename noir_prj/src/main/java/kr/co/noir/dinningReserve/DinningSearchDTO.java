package kr.co.noir.dinningReserve;

import org.apache.ibatis.type.Alias;
import org.eclipse.angus.mail.util.DefaultProvider;

import lombok.Getter;
import lombok.Setter;


@Alias("dinningSearchDTO")
@Setter
@Getter
@DefaultProvider
public class DinningSearchDTO {
	private String dinningType, dinningDate;
}
