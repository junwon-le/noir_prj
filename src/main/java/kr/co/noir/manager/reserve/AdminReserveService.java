package kr.co.noir.manager.reserve;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.noir.room.RoomDomain;

@Service
public class AdminReserveService {

	@Value("${user.crypto.key}")
	private String key;
	
	@Value("${user.crypto.salt}")
	private String salt;
	
	@Autowired(required = false)
	private AdminReserveMapper arm;

	public int totalCnt(AdminResRangeDTO arrDTO) {
		int totalCnt = 0;
		try {
			totalCnt = arm.selectResTotalCnt(arrDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return totalCnt;
	}
	
	public int DinningTotalCnt(AdminResRangeDTO arrDTO) {
		int totalCnt = 0;
		try {
			totalCnt = arm.selectDinningTotalCnt(arrDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return totalCnt;
	}

	/**
	 * 한 화면에 보여줄 페이지의 수
	 * 
	 * @return
	 */
	public int pageScale() {
		return 10;
	}// pageScale

	/**
	 * 총 페지이수
	 * 
	 * @param totalCount 전체 게시물 수
	 * @param pageScale  한 화면에 보여줄 게시물 수
	 * @return
	 */
	public int totalPage(int totalCount, int pageScale) {
		return (int) Math.ceil((double) totalCount / pageScale);
	}// totalPage

	/**
	 * 페이지의 시작번호 구하기
	 * 
	 * @param pageScale   한 화면에 보여줄 게시물 수
	 * @param currentPage 현재 페이지
	 * @return
	 */
	public int startNum(int pageScale, int currentPage) {
		return pageScale * (currentPage - 1) + 1;
	}// startNum

	/**
	 * @param startNum  시작 번호
	 * @param pageScale 한 화면에 보여줄 게시물 수
	 * @return
	 */
	public int endNum(int startNum, int pageScale) {
		return startNum + pageScale - 1;
	}// endNum

	public List<NonRoomResDomain> searchNonRoomList(AdminResRangeDTO arrDTO){
		List<NonRoomResDomain> list = null;
		if(arrDTO.getField() != null) {
			
			String field = switch(arrDTO.getField()) {
			case "예약번호" ->"r.reserve_num";
			case "성명" -> "non_user_res_last_name || non_user_res_first_name";
			case "아이디" -> "r.reserve_email";
			default -> null;
			};//end switch
			arrDTO.setField(field);
		}//end if
		try {
			list = arm.selectNonRoomList(arrDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}//searchNonRoomList
	
	public List<NonRoomResDomain> searchNonDinngingList(AdminResRangeDTO arrDTO){
		List<NonRoomResDomain> list = null;
		if(arrDTO.getField() != null) {
			
			String field = switch(arrDTO.getField()) {
			case "예약번호" ->"r.reserve_num";
			case "성명" -> "non_user_res_last_name || non_user_res_first_name";
			case "아이디" -> "r.reserve_email";
			default -> null;
			};//end switch
			arrDTO.setField(field);
		}//end if
		try {
			list = arm.selectNonDinningList(arrDTO);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}//searchNonRoomList
	
	/**
	 * 페이지 네이션 << 1 2 3 4 5 >>
	 * @param rDTO
	 * @return
	 */
	public String pagination( AdminResRangeDTO arrDTO, String justify ) {
	      StringBuilder pagination=new StringBuilder();
	      //1. 한 화면에 보여줄 pagination의 수.
	      int pageNumber=5;
	      //2. 화면에 보여줄 시작페이지 번호. 1,2,3 => 1로 시작 , 4,5,6=> 4, 7,8,9=>7
	      int startPage= ((arrDTO.getCurrentPage()-1)/pageNumber)*pageNumber+1;
	      //3. 화면에 보여줄 마지막 페이지 번호 1,2,3 =>3
	      int endPage= (((startPage-1)+pageNumber)/pageNumber)*pageNumber;
	      //4. 총페이지수가 연산된 마지막 페이지수보다 작다면 총페이지 수가 마지막 페이지수로 설정
	      //456 인경우 > 4로 설정
	      if( arrDTO.getTotalPage() <= endPage) {
	         endPage=arrDTO.getTotalPage();
	      }//end if
	      //5.첫페이지가 인덱스 화면 (1페이지) 가 아닌 경우
	      int movePage=0;
	      StringBuilder prevMark=new StringBuilder();
	      prevMark.append("<a href='#void' class='arrow'>&lt;</a>");
	      //prevMark.append("<li class='page-item'><a class='page-link' href='#'>Previous</a></li>");
	      if(arrDTO.getCurrentPage() > pageNumber) {// 시작페이지 번호가 3보다 크면 
	         movePage=startPage-1;// 4,5,6->3 ->1 , 7 ,8 ,9 -> 6 -> 4
	         prevMark.delete(0, prevMark.length());// 이전에 링크가 없는 [<<] 삭제
	         prevMark.append("<a href='").append(arrDTO.getUrl())
	         .append("?currentPage=").append(movePage);
	         //검색 키워드가 있다면 검색 키워드를 붙인다.
	         if( arrDTO.getKeyword() != null && !arrDTO.getKeyword().isEmpty() ) {
	            prevMark.append("&field=").append(arrDTO.getField())
	            .append("&keyword=").append(arrDTO.getKeyword());
	         }//end if
	         prevMark.append("'>&lt;</a>");
	      }//end if
	      
	      //6.시작페이지 번호부터 끝 번호까지 화면에 출력
	      StringBuilder pageLink=new StringBuilder();
	      movePage=startPage;
	      
	      while( movePage <= endPage ) {
	         if( movePage == arrDTO.getCurrentPage()) { //현재 페이지 : 링크 x
	            pageLink.append("<a href='#void' class='page-num active'>") 
	            .append(movePage).append("</a>");
	         }else {//현재페이지가 아닌 다른 페이지 : 링크 O
	            pageLink.append("<a href='")
	            .append(arrDTO.getUrl()).append("?currentPage=").append(movePage);
	            //검색 키워드가 있다면 검색 키워드를 붙인다.
	            if( arrDTO.getKeyword() != null && !arrDTO.getKeyword().isEmpty() ) {
	               pageLink.append("&field=").append(arrDTO.getField())
	               .append("&keyword=").append(arrDTO.getKeyword());
	            }//end if
	            pageLink.append("' class='page-num'>").append(movePage).append("</a>");
	            
	         }//else
	         
	         movePage++;
	      }//end while
	      
	      //7. 뒤에 페이지가 더 있는 경우
	      StringBuilder nextMark=new StringBuilder(" <a href='#void' class='arrow'>&gt;</a> ");
	      if( arrDTO.getTotalPage() > endPage) { // 뒤에 페이지가 더 있음.
	         movePage= endPage+1;
	         nextMark.delete(0, nextMark.length());
	         nextMark.append("<a href='")
	         .append(arrDTO.getUrl()).append("?currentPage=").append(movePage);
	         if( arrDTO.getKeyword() != null && !arrDTO.getKeyword().isEmpty() ) {
	            nextMark.append("&field=").append(arrDTO.getField())
	            .append("&keyword=").append(arrDTO.getKeyword());
	         }//end if
	         
	         nextMark.append("'#void' class='arrow'>&gt;</a> ");
	         
	      }//end if
	      
	      if(!("center".equals(justify) || "left".equals(justify))) {
	         justify="left";
	      }
	      pagination.append("<nav aria-label='...'>")
	      .append("  <ul class='pagination d-flex justify-content-")
	      .append(justify)
	      .append("'>");
	      pagination.append(prevMark).append(pageLink).append(nextMark);
	      pagination.append("</ul>")
	      .append("  </nav>");
	      
	      return pagination.toString();
	   }//pagination
	
	/**
	 * 페이지 네이션 << 1 2 3 4 5 >>
	 * @param rDTO
	 * @return
	 */
	public String paginationDinning( AdminResRangeDTO arrDTO, String justify ) {
	      StringBuilder pagination=new StringBuilder();
	      //1. 한 화면에 보여줄 pagination의 수.
	      int pageNumber=5;
	      //2. 화면에 보여줄 시작페이지 번호. 1,2,3 => 1로 시작 , 4,5,6=> 4, 7,8,9=>7
	      int startPage= ((arrDTO.getCurrentPage()-1)/pageNumber)*pageNumber+1;
	      //3. 화면에 보여줄 마지막 페이지 번호 1,2,3 =>3
	      int endPage= (((startPage-1)+pageNumber)/pageNumber)*pageNumber;
	      //4. 총페이지수가 연산된 마지막 페이지수보다 작다면 총페이지 수가 마지막 페이지수로 설정
	      //456 인경우 > 4로 설정
	      if( arrDTO.getTotalPage() <= endPage) {
	         endPage=arrDTO.getTotalPage();
	      }//end if
	      //5.첫페이지가 인덱스 화면 (1페이지) 가 아닌 경우
	      int movePage=0;
	      StringBuilder prevMark=new StringBuilder();
	      prevMark.append("<a href='#void' class='arrow'>&lt;</a>");
	      //prevMark.append("<li class='page-item'><a class='page-link' href='#'>Previous</a></li>");
	      if(arrDTO.getCurrentPage() > pageNumber) {// 시작페이지 번호가 3보다 크면 
	         movePage=startPage-1;// 4,5,6->3 ->1 , 7 ,8 ,9 -> 6 -> 4
	         prevMark.delete(0, prevMark.length());// 이전에 링크가 없는 [<<] 삭제
	         prevMark.append("<a href='").append(arrDTO.getUrlD())
	         .append("?currentPage=").append(movePage);
	         //검색 키워드가 있다면 검색 키워드를 붙인다.
	         if( arrDTO.getKeyword() != null && !arrDTO.getKeyword().isEmpty() ) {
	            prevMark.append("&field=").append(arrDTO.getField())
	            .append("&keyword=").append(arrDTO.getKeyword());
	         }//end if
	         prevMark.append("'>&lt;</a>");
	      }//end if
	      
	      //6.시작페이지 번호부터 끝 번호까지 화면에 출력
	      StringBuilder pageLink=new StringBuilder();
	      movePage=startPage;
	      
	      while( movePage <= endPage ) {
	         if( movePage == arrDTO.getCurrentPage()) { //현재 페이지 : 링크 x
	            pageLink.append("<a href='#void' class='page-num active'>") 
	            .append(movePage).append("</a>");
	         }else {//현재페이지가 아닌 다른 페이지 : 링크 O
	            pageLink.append("<a href='")
	            .append(arrDTO.getUrlD()).append("?currentPage=").append(movePage);
	            //검색 키워드가 있다면 검색 키워드를 붙인다.
	            if( arrDTO.getKeyword() != null && !arrDTO.getKeyword().isEmpty() ) {
	               pageLink.append("&field=").append(arrDTO.getField())
	               .append("&keyword=").append(arrDTO.getKeyword());
	            }//end if
	            pageLink.append("' class='page-num'>").append(movePage).append("</a>");
	            
	         }//else
	         
	         movePage++;
	      }//end while
	      
	      //7. 뒤에 페이지가 더 있는 경우
	      StringBuilder nextMark=new StringBuilder(" <a href='#void' class='arrow'>&gt;</a> ");
	      if( arrDTO.getTotalPage() > endPage) { // 뒤에 페이지가 더 있음.
	         movePage= endPage+1;
	         nextMark.delete(0, nextMark.length());
	         nextMark.append("<a href='")
	         .append(arrDTO.getUrlD()).append("?currentPage=").append(movePage);
	         if( arrDTO.getKeyword() != null && !arrDTO.getKeyword().isEmpty() ) {
	            nextMark.append("&field=").append(arrDTO.getField())
	            .append("&keyword=").append(arrDTO.getKeyword());
	         }//end if
	         
	         nextMark.append("'#void' class='arrow'>&gt;</a> ");
	         
	      }//end if
	      
	      if(!("center".equals(justify) || "left".equals(justify))) {
	         justify="left";
	      }
	      pagination.append("<nav aria-label='...'>")
	      .append("  <ul class='pagination d-flex justify-content-")
	      .append(justify)
	      .append("'>");
	      pagination.append(prevMark).append(pageLink).append(nextMark);
	      pagination.append("</ul>")
	      .append("  </nav>");
	      
	      return pagination.toString();
	   }//pagination
	
	
		//객실 예약 상세
		public List<NonRoomDetailDomain> searchNonRoomDetail(int resNum) {
			List<NonRoomDetailDomain> roomDetailList = arm.selectnonRoomDetail(resNum);
			for(NonRoomDetailDomain roomDetail :roomDetailList) {
			roomDetail.setReserveStartDate(roomDetail.getReserveStartDate().substring(0,10));
			roomDetail.setReserveEndDate(roomDetail.getReserveEndDate().substring(0,10));
			LocalDate start =LocalDate.parse(roomDetail.getReserveStartDate()); 
			LocalDate end =LocalDate.parse(roomDetail.getReserveEndDate());
			long period = ChronoUnit.DAYS.between(start, end);
			TextEncryptor te = Encryptors.text(key,salt);
			roomDetail.setNonUserTel(te.decrypt(roomDetail.getNonUserTel()));
			roomDetail.setPeriod(period);
			}
			return roomDetailList;
		}//searchNonRoomDetail
		
		//객실 예약 상세
		public NonDinningDetailDomain searchnonDinningDetail(int resNum) {
			NonDinningDetailDomain dinningDetail = arm.selectnonDinningDetail(resNum);
			//dinningDetail.setDinningVisitDate(dinningDetail.getDinninVisitDate().substring(0,10));
			TextEncryptor te = Encryptors.text(key,salt);
			dinningDetail.setNonUserTel(te.decrypt(dinningDetail.getNonUserTel()));
			return dinningDetail;
		}//searchNonRoomDetail
		
		//예약 취소
		@Transactional
		public void modifyRes(int resNum) {
			 arm.updateRes(resNum) ;
			 arm.updatePay(resNum);
		}//searchNonRoomDetail
		
	
}//class
