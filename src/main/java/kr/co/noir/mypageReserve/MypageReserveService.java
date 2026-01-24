package kr.co.noir.mypageReserve;

import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.util.JSONPObject;
@Service
public class MypageReserveService {

	@Autowired(required = false)
	private MypageReserveDAO mrDAO;
	
	
	
	
	/**
	 * 검색된 총 게시물의 수 
	 * @param rsDTO
	 * @return
	 */
	public int totalCnt(ReserveSearchDTO rsDTO) {
		int totalCnt=0;
		try {
			totalCnt=mrDAO.selectTotalCnt(rsDTO);
			
		}catch(PersistenceException pe) {
		pe.printStackTrace();
	 }//end catch;
		
		return totalCnt;
	}//totalCnt
	
	/**
	 * 한 화면에 보여줄 페이지의 수
	 * @return
	 */
	public int pageScale() {
		
		return 5;
		
	}//pageScale
	
	/**
	 * 총페이지의 수
	 * @param totalCount - 전체 게시물의 수 
	 * @param pageScale - 한 화면에 보여줄 게시물의 수
	 * @return
	 */
	public int totalPage(int totalCount, int pageScale) {
		return (int)Math.ceil((double)totalCount/pageScale);
		
	}//totalPage
	
	/**
	 * 페이지의 시작번호 구하기
	 * @param currentPage - 현재 페이지
	 * @param pageScale - 한화면에 보여줄 게기물의 수
	 * @return
	 */
	public int startNum(int currentPage , int pageScale) {
		return currentPage * pageScale-pageScale+1;
		
	}//startNum
	
	
	/**
	 * 페이지의 끝번호 구하기
	 * @param startNum - 시작번호
	 * @param pageScale - 한 화면에 보여줄 게시물의 수 
	 * @return
	 */
	public int endNum(int startNum,int pageScale) {
		return startNum+pageScale-1;
		
	}//endNum
	
	
	
	
//	public List<HotelRevSearchDomain> searchHotelRevList(ReserveSearchDTO rsDTO){
//		List<HotelRevSearchDomain> list=null;
//		
//		try {
//			list=mrDAO.selectHotelRevList(rsDTO);
//			
//		}catch (PersistenceException pe) {
//			pe.printStackTrace();
//		}//end catch
//		
//		return list;
//		
//	}//searchHotelRev
	
	public String searchHotelRevList(ReserveSearchDTO rsDTO) {
		List<HotelRevSearchDomain> list =null;
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("flag", false);
		
		int totalCount = totalCnt(rsDTO);
		int pageScale=pageScale();
		int totalPage=totalPage(totalCount, pageScale);
		int currentPage=rsDTO.getCurrentPage();
		int startNum=startNum(currentPage, pageScale);
		int endNum=endNum(startNum, pageScale);
		
		System.out.println("총 게시글 수: " + totalCount);
		System.out.println("총 페이지 수: " + totalPage);
		
		rsDTO.setStartNum(startNum);
		rsDTO.setEndNum(endNum);
		rsDTO.setUrl("/mypage/memberHotelRevList");
		
		
		
		try {
			list=mrDAO.selectHotelRevList(rsDTO);
			jsonObj.put("flag", true);
			JSONObject jsonTemp = null;
			JSONArray jsonArr=new JSONArray();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for(HotelRevSearchDomain hsd : list) {
				jsonTemp= new JSONObject();
				jsonTemp.put("reserveNum", hsd.getReserveNum());
				jsonTemp.put("reservePerson", hsd.getReservePerson());
				jsonTemp.put("reserveDate", sdf.format(hsd.getReserveDate()));
				jsonTemp.put("roomType", hsd.getRoomType());
				jsonTemp.put("checkIn", sdf.format(hsd.getCheckIn()));
				jsonTemp.put("checkOut", sdf.format(hsd.getCheckOut()));
				jsonTemp.put("reserveFlag", hsd.getReserveFlag());
				
				jsonArr.add(jsonTemp);
			}
			jsonObj.put("data", jsonArr);
			jsonObj.put("pagiNation",pagination(rsDTO));
			
			
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		
		}//end catch
		
		
		
		
		return jsonObj.toJSONString();
				
	}//searchHotelRevList
	
//	public String pagination(RangeDTO rDTO) {
//	    StringBuilder pagiNation = new StringBuilder();
//	    
//	    // 1. 한 화면에 보여줄 pagination 번호 개수
//	    int pageNumber = 5; // 스타일 예시에 맞춰 5로 설정 (원하시면 3으로 변경 가능)
//	    
//	    // 2. 시작 및 마지막 페이지 번호 계산
//	    int startPage = ((rDTO.getCurrentPage() - 1) / pageNumber) * pageNumber + 1;
//	    int endPage = startPage + pageNumber - 1;
//	    
//	    // 3. 총 페이지수가 계산된 마지막 페이지보다 작을 경우 조정
//	    if (rDTO.getTotalPage() <= endPage) {
//	        endPage = rDTO.getTotalPage();
//	    }
//	    
//	    // 전체 감싸는 태그 시작
//	    pagiNation.append("<ul class='pagination-wrapper'>");
//
//	    // 4. 이전(Previous) 버튼 처리
//	    // 시작 페이지가 1보다 크면 이전 그룹으로 갈 수 있는 링크 활성화
//	    pagiNation.append("<li class='page-item ").append(startPage <= 1 ? "disabled" : "").append("'>");
//	    if (startPage > 1) {
//	        int prevPage = startPage - 1;
//	        pagiNation.append("<a class='page-link' href='javascript:movePage(").append(prevPage).append(")' aria-label='Previous'>");
//	    } else {
//	        pagiNation.append("<a class='page-link' href='javascript:void(0)' aria-label='Previous'>");
//	    }
//	    // 주신 SVG 아이콘 삽입
//	    pagiNation.append("<svg viewBox='0 0 24 24'><path d='M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z'/></svg></a></li>");
//
//	    // 5. 페이지 숫자 리스트 출력
//	    for (int i = startPage; i <= endPage; i++) {
//	        if (i == rDTO.getCurrentPage()) {
//	            // 현재 보고 있는 페이지 (active 클래스 추가)
//	            pagiNation.append("<li class='page-item active'><a class='page-link' href='javascript:void(0)'>")
//	                      .append(i).append("</a></li>");
//	        } else {
//	            // 클릭 가능한 다른 페이지
//	            pagiNation.append("<li class='page-item'><a class='page-link' href='javascript:movePage(")
//	                      .append(i).append(")'>").append(i).append("</a></li>");
//	        }
//	    }
//
//	    // 6. 다음(Next) 버튼 처리
//	    // 마지막 페이지가 전체 페이지보다 작으면 다음 그룹으로 갈 수 있는 링크 활성화
//	    pagiNation.append("<li class='page-item ").append(endPage >= rDTO.getTotalPage() ? "disabled" : "").append("'>");
//	    if (endPage < rDTO.getTotalPage()) {
//	        int nextPage = endPage + 1;
//	        pagiNation.append("<a class='page-link' href='javascript:movePage(").append(nextPage).append(")' aria-label='Next'>");
//	    } else {
//	        pagiNation.append("<a class='page-link' href='javascript:void(0)' aria-label='Next'>");
//	    }
//	    // 주신 SVG 아이콘 삽입
//	    pagiNation.append("<svg viewBox='0 0 24 24'><path d='M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z'/></svg></a></li>");
//
//	    pagiNation.append("</ul>");
//	    
//	    return pagiNation.toString();
//	}
	
	public String pagination(RangeDTO rDTO) {
	    StringBuilder pagiNation = new StringBuilder();
	    
	    // 1. 한 화면에 보여줄 번호 개수
	    int pageNumber = 5;
	    
	    // 최소 총 페이지 수는 1페이지가 되도록 방어 코드 추가
	    int totalPage = rDTO.getTotalPage();
	    if(totalPage < 1) {
	        totalPage = 1;
	    }
	    
	    // 2. 시작 및 마지막 페이지 계산
	    int startPage = ((rDTO.getCurrentPage() - 1) / pageNumber) * pageNumber + 1;
	    int endPage = startPage + pageNumber - 1;
	    
	    // 3. 마지막 페이지 보정
	    if (totalPage <= endPage) {
	        endPage = totalPage;
	    }
	    
	    pagiNation.append("<ul class='pagination-wrapper'>");

	    // 4. 이전(Previous) 버튼
	    // 첫 번째 그룹(1~5페이지)에 있으면 disabled
	    pagiNation.append("<li class='page-item ").append(startPage == 1 ? "disabled" : "").append("'>");
	    if (startPage > 1) {
	        int prevPage = startPage - 1;
	        pagiNation.append("<a class='page-link' href='javascript:movePage(").append(prevPage).append(")' aria-label='Previous'>");
	    } else {
	        pagiNation.append("<a class='page-link' href='javascript:void(0)' aria-label='Previous'>");
	    }
	    pagiNation.append("<svg viewBox='0 0 24 24'><path d='M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z'/></svg></a></li>");

	    // 5. 페이지 숫자 리스트 (데이터가 1개라도 있으면 무조건 1은 출력됨)
	    for (int i = startPage; i <= endPage; i++) {
	        if (i == rDTO.getCurrentPage()) {
	            pagiNation.append("<li class='page-item active'><a class='page-link' href='javascript:void(0)'>")
	                      .append(i).append("</a></li>");
	        } else {
	            pagiNation.append("<li class='page-item'><a class='page-link' href='javascript:movePage(")
	                      .append(i).append(")'>").append(i).append("</a></li>");
	        }
	    }

	    // 6. 다음(Next) 버튼
	    // 마지막 그룹에 있으면 disabled
	    pagiNation.append("<li class='page-item ").append(endPage >= totalPage ? "disabled" : "").append("'>");
	    if (endPage < totalPage) {
	        int nextPage = endPage + 1;
	        pagiNation.append("<a class='page-link' href='javascript:movePage(").append(nextPage).append(")' aria-label='Next'>");
	    } else {
	        pagiNation.append("<a class='page-link' href='javascript:void(0)' aria-label='Next'>");
	    }
	    pagiNation.append("<svg viewBox='0 0 24 24'><path d='M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z'/></svg></a></li>");

	    pagiNation.append("</ul>");
	    
	    return pagiNation.toString();
	}
	
}
