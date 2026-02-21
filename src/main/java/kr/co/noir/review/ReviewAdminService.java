package kr.co.noir.review;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewAdminService {

    @Autowired
    private ReviewAdminDAO raDAO;

    /* =========================
       1. 총 게시글 수
       ========================= */
    public int totalCnt(ReviewRangeDTO rrDTO) {
        try {
            if (rrDTO.getRoomTypeNum() > 0) {
                return raDAO.selectRoomReviewCnt(rrDTO);
            } else {
                return raDAO.selectReviewTotalCnt(rrDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /* =========================
       2. 페이지 설정
       ========================= */
    public int pageScale() {
        return 4;   // 한 페이지당 게시글 수
    }

    public int totalPage(int totalCount, int pageScale) {
        return (int) Math.ceil((double) totalCount / pageScale);
    }

    public int startNum(int currentPage, int pageScale) {
        return currentPage * pageScale - pageScale + 1;
    }

    public int endNum(int startNum, int pageScale) {
        return startNum + pageScale - 1;
    }

    /* =========================
       3. 목록 조회
       ========================= */
    public List<ReviewAdminDomain> getReviewAdminList(ReviewRangeDTO rrDTO) {
        try {
            if (rrDTO.getRoomTypeNum() > 0) {
                return raDAO.selectReviewByRoom(rrDTO);
            } else {
                return raDAO.selectReviewList(rrDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* =========================
       4. 상세 조회
       ========================= */
    public ReviewAdminDomain searchOneReview(int reviewNum) {
        try {
            return raDAO.selectReviewDetail(reviewNum);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* =========================
       5. 답변 등록/수정
       ========================= */
    public boolean replyReview(ReviewAdminDTO raDTO) {
        try {
            return raDAO.updateReplyReview(raDTO) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* =========================
       6. 리뷰 Soft Delete
       ========================= */
    public boolean removeReview(int reviewNum) {
        try {
            return raDAO.deleteAdminReview(reviewNum) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* =========================
       7. 답변만 삭제
       ========================= */
    public boolean removeOnlyReply(int reviewNum) {
        try {
            return raDAO.deleteOnlyReply(reviewNum) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* =========================
       8. 리뷰 이미지 리스트
       ========================= */
    public List<String> searchReviewImgList(int reviewNum) {
        try {
            return raDAO.selectReviewImgList(reviewNum);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* =========================
       9. 페이징 HTML 생성
       ========================= */
    public String pagination(ReviewRangeDTO rrDTO, String justify) {

        StringBuilder pagination = new StringBuilder();

        int pageNumber = 3;
        int startPage = ((rrDTO.getCurrentPage() - 1) / pageNumber) * pageNumber + 1;
        int endPage = startPage + pageNumber - 1;

        if (endPage > rrDTO.getTotalPage()) endPage = rrDTO.getTotalPage();

        // ✅ roomTypeNum 있으면 항상 num 파라미터 유지
        boolean hasRoomFilter = rrDTO.getRoomTypeNum() > 0;

        // ✅ 링크 베이스
        String baseUrl = rrDTO.getUrl();
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "/admin/review/list";
        }

        int movePage;

        // 이전
        StringBuilder prevMark = new StringBuilder();
        if (rrDTO.getCurrentPage() > pageNumber) {
            movePage = startPage - 1;
            prevMark.append("<li class='page-item'><a class='page-link' href='")
                    .append(baseUrl)
                    .append("?currentPage=").append(movePage);

            if (hasRoomFilter) {
                prevMark.append("&num=").append(rrDTO.getRoomTypeNum());
            }
            prevMark.append("'>이전</a></li>");
        } else {
            prevMark.append("<li class='page-item disabled'><span class='page-link'>이전</span></li>");
        }

        // 페이지 번호
        StringBuilder pageLink = new StringBuilder();
        movePage = startPage;

        while (movePage <= endPage) {
            if (movePage == rrDTO.getCurrentPage()) {
                pageLink.append("<li class='page-item active'><span class='page-link'>")
                        .append(movePage).append("</span></li>");
            } else {
                pageLink.append("<li class='page-item'><a class='page-link' href='")
                        .append(baseUrl)
                        .append("?currentPage=").append(movePage);

                if (hasRoomFilter) {
                    pageLink.append("&num=").append(rrDTO.getRoomTypeNum());
                }

                pageLink.append("'>").append(movePage).append("</a></li>");
            }
            movePage++;
        }

        // 다음
        StringBuilder nextMark = new StringBuilder();
        if (rrDTO.getTotalPage() > endPage) {
            movePage = endPage + 1;
            nextMark.append("<li class='page-item'><a class='page-link' href='")
                    .append(baseUrl)
                    .append("?currentPage=").append(movePage);

            if (hasRoomFilter) {
                nextMark.append("&num=").append(rrDTO.getRoomTypeNum());
            }
            nextMark.append("'>다음</a></li>");
        } else {
            nextMark.append("<li class='page-item disabled'><span class='page-link'>다음</span></li>");
        }

        pagination.append("<nav><ul class='pagination d-flex justify-content-")
                .append(justify).append("'>")
                .append(prevMark)
                .append(pageLink)
                .append(nextMark)
                .append("</ul></nav>");

        return pagination.toString();
    
    }
}