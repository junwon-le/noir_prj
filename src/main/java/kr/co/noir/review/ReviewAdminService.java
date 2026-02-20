package kr.co.noir.review;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ReviewAdminService {

	@Autowired
	private ReviewAdminDAO raDAO;
	
	/**
	 * 총 게시물의 수
	 * @param rrDTO
	 * @return
	 */
	public int totalCnt(ReviewRangeDTO rrDTO) {
        int totalCnt = 0;
        try {
            // roomTypeNum > 0이면 객실 필터 카운트, 아니면 전체 카운트
            if (rrDTO.getRoomTypeNum() > 0) {
                totalCnt = raDAO.selectRoomReviewCnt(rrDTO);
            } else {
                totalCnt = raDAO.selectReviewTotalCnt(rrDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCnt;
	}//totalCnt
	
	
	//객실타입으로 필터링된 리뷰의 총 개수
	public int roomReviewTotalCnt(ReviewRangeDTO rrDTO) {
		int totalCnt=0;
		try {
			totalCnt=raDAO.selectRoomReviewCnt(rrDTO);
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
	
	
	
	
	public List<ReviewAdminDomain> getReviewAdminDomains(ReviewRangeDTO rrDTO) {
	    List<ReviewAdminDomain> list = null;
	    try {
	        // 검색어(keyword)가 비어있어도 rrDTO의 start/endNum으로 페이징은 돌아감
	        list = raDAO.selectReviewList(rrDTO); 
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
	public List<ReviewAdminDomain> getReviewRoomList(ReviewRangeDTO rrDTO) {
		List<ReviewAdminDomain> list = null;
		try {
			// 검색어(keyword)가 비어있어도 rrDTO의 start/endNum으로 페이징은 돌아감
			list = raDAO.selectReviewByRoom(rrDTO); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	

	/**
	 * 글 상세 보기
	 * @param num
	 * @return
	 */
	public ReviewAdminDomain searchOneReview(int reviewNum) {
		ReviewAdminDomain raDomain = null;
		try {
			raDomain= raDAO.selectReviewDetail(reviewNum);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return raDomain;
	}


	// 리뷰 답변 등록/수정
	public boolean replyReview(ReviewAdminDTO raDTO) {
	    try {
	        return raDAO.updateReplyReview(raDTO) == 1;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	// 리뷰 soft delete
	public boolean removeReview(int reviewNum) {
	    try {
	        return raDAO.deleteAdminReview(reviewNum) == 1;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	// 답변만 삭제
	public boolean removeOnlyReply(int reviewNum) {
	    try {
	        return raDAO.deleteOnlyReply(reviewNum) == 1;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	// 리뷰 이미지 목록 조회
	public List<String> searchReviewImgList(int reviewNum) {
	    try {
	        return raDAO.selectReviewImgList(reviewNum);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	
	public String pagination2(ReviewRangeDTO rrDTO, String justify) {
	    StringBuilder pagination = new StringBuilder();
	    int pageNumber = 3;
	    int startPage = ((rrDTO.getCurrentPage() - 1) / pageNumber) * pageNumber + 1;
	    int endPage = (((startPage - 1) + pageNumber) / pageNumber) * pageNumber;
	    if (rrDTO.getTotalPage() <= endPage) { endPage = rrDTO.getTotalPage(); }

	    // 현재 URL이 객실 리뷰용인지 확인
	    boolean isRoomReview = rrDTO.getUrl().contains("roomReviewList");

	    int movePage = 0;
	    StringBuilder prevMark = new StringBuilder("<li class='page-item prev disabled'><a class='page-link'>이전</a></li>");
	    if (rrDTO.getCurrentPage() > pageNumber) {
	        movePage = startPage - 1;
	        prevMark.setLength(0);
	        prevMark.append("<li class='page-item prev'><a class='page-link' href='").append(rrDTO.getUrl())
	                .append("?currentPage=").append(movePage);
	        if (isRoomReview && rrDTO.getRoomTypeNum() != 0) { prevMark.append("&num=").append(rrDTO.getRoomTypeNum()); }
	        prevMark.append("'>이전</a></li>");
	    }

	    StringBuilder pageLink = new StringBuilder();
	    movePage = startPage;
	    while (movePage <= endPage) {
	        if (movePage == rrDTO.getCurrentPage()) {
	            pageLink.append("<li class='page-item active page-link'>").append(movePage).append("</li>");
	        } else {
	            pageLink.append("<li class='page-item'><a class='page-link' href='")
	                    .append(rrDTO.getUrl()).append("?currentPage=").append(movePage);
	            if (isRoomReview && rrDTO.getRoomTypeNum() != 0) { pageLink.append("&num=").append(rrDTO.getRoomTypeNum()); }
	            pageLink.append("'>").append(movePage).append("</a></li>");
	        }
	        movePage++;
	    }

	    StringBuilder nextMark = new StringBuilder("<li class='page-item next disabled'><span class='page-link'>다음</span></li>");
	    if (rrDTO.getTotalPage() > endPage) {
	        movePage = endPage + 1;
	        nextMark.setLength(0);
	        nextMark.append("<li class='page-item next'><a class='page-link' href='")
	                .append(rrDTO.getUrl()).append("?currentPage=").append(movePage);
	        if (isRoomReview && rrDTO.getRoomTypeNum() != 0) { nextMark.append("&num=").append(rrDTO.getRoomTypeNum()); }
	        nextMark.append("'>다음</a></li>");
	    }

	    pagination.append("<nav aria-label='...'><ul class='pagination d-flex justify-content-")
	              .append(justify).append("'>")
	              .append(prevMark).append(pageLink).append(nextMark)
	              .append("</ul></nav>");
	    return pagination.toString();
	}

	
}//class
