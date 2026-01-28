package kr.co.noir.room;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

@Controller
public class RoomController {

	@Autowired
	private RoomService rService;
	
	
	/**
	 * 사용자 숙소 보기
	 * @param num
	 * @param model
	 * @return
	 */
	@GetMapping("/roomView")
	public String roomView(String num, Model model) {
		
		
		int number =  Integer.parseInt(num);
		model.addAttribute("room",rService.searchRoom(number));
		
		return "/room/room";
	}
	
	
	
	/**
	 * 사용자 숙소 자세히 보기
	 * @param num
	 * @param model
	 * @return
	 */
	@GetMapping("/roomDetailView")
	public String roomDetailView(String num, Model model) {
		
		int number =  Integer.parseInt(num);
		model.addAttribute("room",rService.searchDetailRoom(number));
		
		
		return "/room/roomDetail";
	}
	
	/**
	 * 관리자 객실 관리 뷰
	 * @param model
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/admin/roomMgr",method= {GET,POST})
	public String roomMgr(Model model, HttpSession session, String date) {
		
		//session 체크 필요
		
		List<RoomDomain> list = null;
		list = rService.searchRoomList();
		for(int i=0;i<list.size();i++) {
			String detail=list.get(i).getRoomDetail();
			if(detail.length()>42) {
				list.get(i).setRoomDetail(detail.substring(0,41)+"...");
			}
			
		}
		
		List<RoomPriceDomain> rpDomain=null;
		List<RoomPriceDomain> rpDomain2=null;
		
		rpDomain=rService.searchTodayRoomPrice();
		rpDomain2=rService.searchDayRoomPrice(date);
		
		model.addAttribute("room",list);
		model.addAttribute("todayPrice",rpDomain);
		model.addAttribute("selectPrice",rpDomain2);
		
		
		
		return "/manager/room/roomManage";
	}
	
	
	
	
	@GetMapping("/admin/roomMgrModify")
	public String roomMgrModify(String num, Model model) {
		int number =  Integer.parseInt(num);
		model.addAttribute("room",rService.searchDetailRoom(number));
		
		return "/manager/room/roomManageModify";
	}
	
	@Value("${user.upload-dir}")
	private String uploadDir;

	
	@PostMapping("/admin/roomModifyProcess")
	public String roomModfiyProcess(@RequestParam(value="roomImgFile1") MultipartFile mf1,
			@RequestParam(value="roomImgFile2") MultipartFile mf2,
			@RequestParam(value="roomImgFile3") MultipartFile mf3,
			@RequestParam(value="roomImgFile4") MultipartFile mf4,
			@RequestParam(value="roomImgFile5") MultipartFile mf5,	
			RoomDTO rDTO, Model model, String num) throws IOException {
		
		//System.out.println( mf1.getContentType() );
		//System.out.println( mf1.getName() );
		//System.out.println( mf1.getOriginalFilename() );
		//System.out.println( mf1.getSize() );
//		System.out.println( mf.isEmpty() );
//		String fileName =System.currentTimeMillis()+"-"+mf.getOriginalFilename();
		rDTO.setRoomTypeNum(Integer.parseInt(num));
		
		if(mf1.getOriginalFilename()!=null && !mf1.getOriginalFilename().isEmpty()) {
			String fileName1 =UUID.randomUUID()+"-"+mf1.getOriginalFilename();
			File upFile1 = new File(uploadDir+fileName1);
			mf1.transferTo(upFile1);
			rDTO.setRoomImg1(fileName1);
		}
		
		
		if(mf2.getOriginalFilename()!=null && !mf2.getOriginalFilename().isEmpty()) {
			String fileName2 =UUID.randomUUID()+"-"+mf2.getOriginalFilename();
			File upFile2 = new File(uploadDir+fileName2);
			mf2.transferTo(upFile2);
			rDTO.setRoomImg2(fileName2);
		}
		if(mf3.getOriginalFilename()!=null && !mf3.getOriginalFilename().isEmpty()) {
			String fileName3 =UUID.randomUUID()+"-"+mf3.getOriginalFilename();
			File upFile3 = new File(uploadDir+fileName3);
			mf3.transferTo(upFile3);
			rDTO.setRoomImg3(fileName3);
		}
		
		if(mf4.getOriginalFilename()!=null && !mf4.getOriginalFilename().isEmpty()) {
			String fileName4=UUID.randomUUID()+"-"+mf4.getOriginalFilename();
			File upFile4 = new File(uploadDir+fileName4);
			mf4.transferTo(upFile4);
			rDTO.setRoomImg4(fileName4);
		}
		
		if(mf5.getOriginalFilename()!=null && !mf5.getOriginalFilename().isEmpty()) {
			String fileName5=UUID.randomUUID()+"-"+mf5.getOriginalFilename();
			File upFile5 = new File(uploadDir+fileName5);
			mf5.transferTo(upFile5);
			rDTO.setRoomImg5(fileName5);			
		}
		
		rDTO.setRoomAmenity1(rDTO.getRoomAmenity1()!=null?"Y":"N");
		rDTO.setRoomAmenity2(rDTO.getRoomAmenity2()!=null?"Y":"N");
		rDTO.setRoomAmenity3(rDTO.getRoomAmenity3()!=null?"Y":"N");
		rDTO.setRoomAmenity4(rDTO.getRoomAmenity4()!=null?"Y":"N");
		rDTO.setRoomAmenity5(rDTO.getRoomAmenity5()!=null?"Y":"N");
		
		rDTO.setRoomService1(rDTO.getRoomService1()!=null?"Y":"N");
		rDTO.setRoomService2(rDTO.getRoomService2()!=null?"Y":"N");
		rDTO.setRoomService3(rDTO.getRoomService3()!=null?"Y":"N");
		rDTO.setRoomService4(rDTO.getRoomService4()!=null?"Y":"N");
		rDTO.setRoomService5(rDTO.getRoomService5()!=null?"Y":"N");
		rDTO.setRoomService6(rDTO.getRoomService6()!=null?"Y":"N");
		rDTO.setRoomService7(rDTO.getRoomService7()!=null?"Y":"N");
		rDTO.setRoomService8(rDTO.getRoomService8()!=null?"Y":"N");
		rDTO.setRoomService9(rDTO.getRoomService9()!=null?"Y":"N");
		rDTO.setRoomService10(rDTO.getRoomService10()!=null?"Y":"N");
		
		
		int cnt = 0;
		cnt = rService.modifyRoom(rDTO);
		
		
//		File upFile = new File("C:/dev/workspace/spring_mvc/src/main/resources/static/upload/"+fileName);
		
		//mf.transferTo(upFile); //파일 업로드를 수행
		//FileDTO에 업로드된 파일명을 설정.
		//rDTO.setUpFileName(fileName);		
		model.addAttribute("cnt", cnt);
		
		
		
		
		return "/manager/room/roomMgrModifyProcess";
	}
	
	
	
	
	@GetMapping("/admin/roomManagePrice")
	public String roomPrice() {
		
		return "/manager/room/roomManagePrice";
	}
	
	
	@ResponseBody
	@GetMapping("/room/calandarProcess")
	public JSONObject calandarProcess(RoomPriceDTO rpDTO) {

		JSONObject jsonObj = new JSONObject();

	    YearMonth ym = YearMonth.parse(rpDTO.getRoomPriceDate());
	    LocalDate first = ym.atDay(1);

	    jsonObj.put("year", ym.getYear());
	    jsonObj.put("month", ym.getMonthValue());
	    jsonObj.put("firstDay", first.getDayOfWeek().getValue() % 7);
	    jsonObj.put("lastDate", ym.lengthOfMonth());

	    List<RoomPriceDomain> list = rService.searchRoomPriceView(rpDTO);
	    
	    jsonObj.put("priceList", list);

	    return jsonObj;
	}
	
	@GetMapping("/admin/roomManagePriceProcess")
	public String roomManagePriceProcess(@RequestParam("roomPrices") List<Integer> roomPriceList, @RequestParam("roomPriceDates") List<Integer> roomPriceDate, RoomPriceDTO rDTO, Model model) {
		
		int cnt=0;
		JSONArray jsonArr = new JSONArray();
		String msg ="실패";
		  for(int i=0; i<roomPriceList.size(); i++) { 
			  JSONObject jsonObj = new JSONObject();
			  if(i<9) {
				  jsonObj.put("date", rDTO.getRoomPriceMonth()+"-0"+(i+1));
			  }else {
				  jsonObj.put("date", rDTO.getRoomPriceMonth()+"-"+(i+1));
			  }
			  
			  jsonObj.put("price", rDTO.getRoomPrice());			  
			  
			   
			  for(int j=0; j<roomPriceDate.size();j++) {				  
				  if(roomPriceDate.get(j)==i+1) {
					  jsonObj.put("flag", "on");
					  jsonArr.add(jsonObj);
					  break;
				  }
			  }
		  }
		  
		  
		  for(int i=0; i<jsonArr.size();i++) {
			  JSONObject obj = new JSONObject();
			  obj = (JSONObject)jsonArr.get(i);
			  String parseDate = (String)obj.get("date");
			  Integer parsePrice = (Integer)obj.get("price");
			  RoomPriceDTO rpDTO = new RoomPriceDTO();
			  rpDTO.setRoomPriceDate(parseDate);
			  rpDTO.setRoomPrice(parsePrice);
			  
			  cnt = rService.modfiyRoomPrice(rpDTO);
			  if(cnt==1) {
				  msg="성공";
			  }
		  }
		  
		  model.addAttribute("msg",msg);
		  model.addAttribute("num",rDTO.getRoomTypeNum());
		  
		  
		  
		return "/manager/room/roomManagePriceProcess";
	}
	
	
	
	
}
