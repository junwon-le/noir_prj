package kr.co.noir.reserve;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/reserve")
@Controller
public class ReserveController {
	
	@Autowired
	private RoomReserveService rrs;
	
	@GetMapping("/roomReserve")
	public String reserve() {
		return "/reserve/roomRes";
	}
	@GetMapping("/nonRoomReserve")
	public String nonReserve() {
		return "/reserve/nonRoomRes";
	}
	
	@GetMapping("/roomResSearch")
	public String roomResSearch(RoomSearchDTO rsDTO, Model model) {
		
		return "/reserve/roomResSearch";
	}
	
	@GetMapping("/dinningResSearch")
	public String dinningResSearch() {
		return "/reserve/dinningResSearch";
	}
	@GetMapping("/dinningRes")
	public String dinningRes() {
		return "/reserve/dinningRes";
	}
	@GetMapping("/nonDinningRes")
	public String nonDinningRes() {
		return "/reserve/nonDinningRes";
	}
	
	@GetMapping("/admin/nonRoomRes")
	public String adminNonRoomRes() {
		return "/manager/reserve/nonRoomRes";
	}
	@GetMapping("/admin/nonDinningRes")
	public String adminNonDinningRes() {
		return "/manager/reserve/nonDinningRes";
	}
	
	@ResponseBody
	@GetMapping("/RoomSearchProcess")
	public List<RoomSearchDomain> roomSearchProcess(RoomSearchDTO rsDTO) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		long period = 0;
		try {
			Date startDate= sdf.parse(rsDTO.getStartDate());
			Date endDate= sdf.parse(rsDTO.getEndDate());
			long diffInMillies = endDate.getTime() - startDate.getTime();
			period = diffInMillies / (24 * 60 * 60 * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<RoomSearchDomain> list = rrs.searchRoom(rsDTO);
		for(RoomSearchDomain rsd :list) {
			rsd.setPeriod(period);
		}
		return list;
	}
}
