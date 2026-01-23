package kr.co.noir.mypageReserve;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
	
	
	
	
	public List<HotelRevSearchDomain> searchHotelRev(ReserveSearchDTO rsDTO){
		List<HotelRevSearchDomain> list=null;
		
		try {
			list=mrDAO.selectHotelRevList(rsDTO);
			
		}catch (PersistenceException pe) {
			pe.printStackTrace();
		}//end catch
		
		return list;
		
	}//searchHotelRev
}
