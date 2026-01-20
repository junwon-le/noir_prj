/*
 * package kr.co.noir.event;
 * 
 * import java.util.List;
 * 
 * import org.springframework.ui.Model; import
 * org.springframework.web.bind.annotation.GetMapping; import
 * org.springframework.web.bind.annotation.RequestParam;
 * 
 * public class EventController {
 * 
 * @GetMapping("/evetList") public String eventList(
 * 
 * @RequestParam(defaultValue = "1") int currentPage,
 * 
 * @RequestParam(required = false) String keyword,
 * 
 * @RequestParam(required = false) String category, Model model) {
 * 
 * //NoticeService service = NoticeService.getInstance();
 * 
 * EventRangeDTO rDTO = new EventRangeDTO(); rDTO.setCurrentPage(currentPage);
 * rDTO.setKeyword(keyword); rDTO.setCategory(category);
 * 
 * // 1. 전체 개수 int totalCnt = service.searchNoticeTotalCnt(rDTO);
 * 
 * // 2. 페이징 계산 int pageScale = service.pageScale(); int totalPage =
 * service.totalPage(totalCnt, pageScale); rDTO.setTotalPage(totalPage);
 * 
 * // 3. 리스트 조회용 범위 rDTO.setStartNum(service.startNum(currentPage, pageScale));
 * rDTO.setEndNum(service.endNum(rDTO.getStartNum(), pageScale));
 * 
 * // 4. 데이터 조회 List<NoticeDTO> list = service.searchNotice(rDTO);
 * 
 * // 5. pagination HTML 생성 String pagination = service.pagination(rDTO);
 * 
 * // 6. View로 전달 model.addAttribute("list", list);
 * model.addAttribute("pagination", pagination);
 * 
 * return "event/evetList"; // ← 네 HTML 경로 }
 * 
 * 
 * }
 */