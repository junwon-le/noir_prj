package kr.co.noir.review;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewAdminService {

    @Autowired
    private ReviewAdminDAO raDAO;

    public int totalCnt(ReviewRangeDTO rrDTO) {
        int totalCnt = 0;
        try {
            if (rrDTO.getRoomTypeNum() > 0) {
                totalCnt = raDAO.selectRoomReviewCnt(rrDTO);
            } else {
                totalCnt = raDAO.selectReviewTotalCnt(rrDTO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCnt;
    }

    public int roomReviewTotalCnt(ReviewRangeDTO rrDTO) {
        int totalCnt = 0;
        try {
            totalCnt = raDAO.selectRoomReviewCnt(rrDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalCnt;
    }

    public int pageScale() {
        return 4;
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

    public List<ReviewAdminDomain> getReviewAdminDomains(ReviewRangeDTO rrDTO) {
        List<ReviewAdminDomain> list = null;
        try {
            list = raDAO.selectReviewList(rrDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<ReviewAdminDomain> getReviewRoomList(ReviewRangeDTO rrDTO) {
        List<ReviewAdminDomain> list = null;
        try {
            list = raDAO.selectReviewByRoom(rrDTO);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ReviewAdminDomain searchOneReview(int reviewNum) {
        ReviewAdminDomain raDomain = null;
        try {
            raDomain = raDAO.selectReviewDetail(reviewNum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return raDomain;
    }

    public boolean replyReview(ReviewAdminDTO raDTO) {
        try {
            return raDAO.updateReplyReview(raDTO) == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeReview(int reviewNum) {
        try {
            return raDAO.deleteAdminReview(reviewNum) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeOnlyReply(int reviewNum) {
        try {
            return raDAO.deleteOnlyReply(reviewNum) == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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

        // ✅ 관리자 객실필터 페이지인지 판단 (URL 통일 기준)
        boolean isRoomReview = rrDTO.getUrl() != null && rrDTO.getUrl().contains("/admin/review/adminRoomList\"");

        int movePage = 0;

        // 이전
        StringBuilder prevMark = new StringBuilder("<li class='page-item prev disabled'><a class='page-link'>이전</a></li>");
        if (rrDTO.getCurrentPage() > pageNumber) {
            movePage = startPage - 1;
            prevMark.setLength(0);
            prevMark.append("<li class='page-item prev'><a class='page-link' href='")
                    .append(rrDTO.getUrl())
                    .append("?currentPage=").append(movePage);

            if (isRoomReview && rrDTO.getRoomTypeNum() != 0) {
                prevMark.append("&num=").append(rrDTO.getRoomTypeNum());
            }
            prevMark.append("'>이전</a></li>");
        }

        // 페이지 링크
        StringBuilder pageLink = new StringBuilder();
        movePage = startPage;

        while (movePage <= endPage) {
            if (movePage == rrDTO.getCurrentPage()) {
                pageLink.append("<li class='page-item active page-link'>").append(movePage).append("</li>");
            } else {
                pageLink.append("<li class='page-item'><a class='page-link' href='")
                        .append(rrDTO.getUrl())
                        .append("?currentPage=").append(movePage);

                if (isRoomReview && rrDTO.getRoomTypeNum() != 0) {
                    pageLink.append("&num=").append(rrDTO.getRoomTypeNum());
                }
                pageLink.append("'>").append(movePage).append("</a></li>");
            }
            movePage++;
        }

        // 다음
        StringBuilder nextMark = new StringBuilder("<li class='page-item next disabled'><span class='page-link'>다음</span></li>");
        if (rrDTO.getTotalPage() > endPage) {
            movePage = endPage + 1;
            nextMark.setLength(0);
            nextMark.append("<li class='page-item next'><a class='page-link' href='")
                    .append(rrDTO.getUrl())
                    .append("?currentPage=").append(movePage);

            if (isRoomReview && rrDTO.getRoomTypeNum() != 0) {
                nextMark.append("&num=").append(rrDTO.getRoomTypeNum());
            }
            nextMark.append("'>다음</a></li>");
        }

        pagination.append("<nav aria-label='...'><ul class='pagination d-flex justify-content-")
                .append(justify).append("'>")
                .append(prevMark).append(pageLink).append(nextMark)
                .append("</ul></nav>");

        return pagination.toString();
    }

}
