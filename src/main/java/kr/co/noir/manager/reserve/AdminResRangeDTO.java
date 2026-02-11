package kr.co.noir.manager.reserve;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Alias("adminResRangeDTO")

@Setter
@ToString
@Getter
public class AdminResRangeDTO {
	private int startNum, endNum, currentPage=1, totalPage=0;
	private String field, keyword="", url="/admin/nonRoomReserve",urlD="/admin/nonDinningReserve", type;
}
