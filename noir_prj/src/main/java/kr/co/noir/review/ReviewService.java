package kr.co.noir.review;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.noir.notice.BoardRangeDTO;

@Service
public class ReviewService {

	@Autowired
	private ReviewDAO rDAO;
	
	@Autowired
	private ReviewMapper rm;
	
	/**
	 * 총 게시물의 수
	 * @param rDTO
	 * @return
	 */
	public int totalCnt(BoardRangeDTO rDTO) {
		int totalCnt=0;
		try {
			totalCnt=rDAO.selectBoardTotalCnt(rDTO);
		} catch (SQLException e) {
			e.printStackTrace();
		}//end catch
		
		return totalCnt;
	}//totalCnt
	
	public int roomTotalCnt(BoardRangeDTO rDTO) {
		int totalCnt=0;
		try {
			totalCnt=rDAO.selectRoomReviewCnt(rDTO);
		} catch (SQLException e) {
			e.printStackTrace();
		}//end catch
		
		return totalCnt;
	}//roomTotalCnt
	
	/**
	 * 한 화면에 보여줄 페이지의 수
	 * @return
	 */
	public int pageScale() {
		return 4;
	}//pageScale
	
	/**
	 * 총 페이지 수
	 * @param totalCount - 전체 게시물의 수
	 * @param pageScale - 한 화면에 보여줄 게시물의 수
	 * @return
	 */
	public int totalPage(int totalCount, int pageScale) {
		return (int)Math.ceil((double)totalCount/pageScale);
	}//totalPage
	
	/**
	 * 페이지의 시작번호 구하기
	 * @param currentPage - 현재페이지
	 * @param pageScale - 한 화면에 보여줄 게시물의 수
	 * @return
	 */
	public int startNum(int currentPage, int pageScale) {
		return currentPage * pageScale-pageScale+1;
	}//startNum
	
	/**
	 * 페이지의 끝 번호 구하기
	 * @param startNum - 시작번호
	 * @param pageScale - 한 화면에 보여줄 게시물의 수
	 * @return
	 */
	public int endNum(int startNum, int pageScale) {
		return startNum+pageScale-1;
	}//endNum
	
	public List<ReviewDomain> getReviewList(BoardRangeDTO rDTO) {
	    List<ReviewDomain> list = null;
	    try {
	        // 검색어(keyword)가 비어있어도 rDTO의 start/endNum으로 페이징은 돌아감
	        list = rDAO.selectReviewByMember(rDTO); 
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
	public List<ReviewDomain> getReviewRoomList(BoardRangeDTO rDTO) {
		List<ReviewDomain> list = null;
		try {
			// 검색어(keyword)가 비어있어도 rDTO의 start/endNum으로 페이징은 돌아감
			list = rDAO.selectReviewByRoom(rDTO); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public String pagination2(BoardRangeDTO rDTO, String justify) {
	    StringBuilder pagination = new StringBuilder();
	    int pageNumber = 3;
	    int startPage = ((rDTO.getCurrentPage() - 1) / pageNumber) * pageNumber + 1;
	    int endPage = (((startPage - 1) + pageNumber) / pageNumber) * pageNumber;
	    if (rDTO.getTotalPage() <= endPage) { endPage = rDTO.getTotalPage(); }

	    // 현재 URL이 객실 리뷰용인지 확인
	    boolean isRoomReview = rDTO.getUrl().contains("roomReviewList");

	    int movePage = 0;
	    StringBuilder prevMark = new StringBuilder("<li class='page-item prev disabled'><a class='page-link'>이전</a></li>");
	    if (rDTO.getCurrentPage() > pageNumber) {
	        movePage = startPage - 1;
	        prevMark.setLength(0);
	        prevMark.append("<li class='page-item prev'><a class='page-link' href='").append(rDTO.getUrl())
	                .append("?currentPage=").append(movePage);
	        if (isRoomReview && rDTO.getRoomTypeNum() != 0) { prevMark.append("&num=").append(rDTO.getRoomTypeNum()); }
	        prevMark.append("'>이전</a></li>");
	    }

	    StringBuilder pageLink = new StringBuilder();
	    movePage = startPage;
	    while (movePage <= endPage) {
	        if (movePage == rDTO.getCurrentPage()) {
	            pageLink.append("<li class='page-item active page-link'>").append(movePage).append("</li>");
	        } else {
	            pageLink.append("<li class='page-item'><a class='page-link' href='")
	                    .append(rDTO.getUrl()).append("?currentPage=").append(movePage);
	            if (isRoomReview && rDTO.getRoomTypeNum() != 0) { pageLink.append("&num=").append(rDTO.getRoomTypeNum()); }
	            pageLink.append("'>").append(movePage).append("</a></li>");
	        }
	        movePage++;
	    }

	    StringBuilder nextMark = new StringBuilder("<li class='page-item next disabled'><span class='page-link'>다음</span></li>");
	    if (rDTO.getTotalPage() > endPage) {
	        movePage = endPage + 1;
	        nextMark.setLength(0);
	        nextMark.append("<li class='page-item next'><a class='page-link' href='")
	                .append(rDTO.getUrl()).append("?currentPage=").append(movePage);
	        if (isRoomReview && rDTO.getRoomTypeNum() != 0) { nextMark.append("&num=").append(rDTO.getRoomTypeNum()); }
	        nextMark.append("'>다음</a></li>");
	    }

	    pagination.append("<nav aria-label='...'><ul class='pagination d-flex justify-content-")
	              .append(justify).append("'>")
	              .append(prevMark).append(pageLink).append(nextMark)
	              .append("</ul></nav>");
	    return pagination.toString();
	}
	   
	public int getMemberNumById(String memberId) {
	    return rm.selectMemberNum(memberId);
	}
	
	
	/**
	 * 글 상세 보기
	 * @param num
	 * @return
	 */
	public ReviewDomain searchOneBoard(int inquiryNum) {
		ReviewDomain iDomain = null;
		try {
			iDomain= rDAO.selectBoardDetail(inquiryNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return iDomain;
	}
	
	
}//class
