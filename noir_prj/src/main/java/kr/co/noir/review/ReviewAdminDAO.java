package kr.co.noir.review;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository("reviewAdminDAO")
public class ReviewAdminDAO {

    // ✅ 관리자 전용 MyBatis namespace (사용자와 충돌 방지)
    private static final String NS = "kr.co.noir.review.admin.ReviewAdminMapper.";

    // 전체 총 게시물 수
    public int selectReviewTotalCnt(ReviewRangeDTO rrDTO) throws SQLException {
        int totalCnt = 0;
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            totalCnt = ss.selectOne(NS + "selectReviewTotalCnt", rrDTO);
        } finally {
            if (ss != null) { ss.close(); }
        }
        return totalCnt;
    }

    // 객실타입으로 필터링된 리뷰의 총 개수
    public int selectRoomReviewCnt(ReviewRangeDTO rrDTO) throws SQLException {
        int totalCnt = 0;
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            totalCnt = ss.selectOne(NS + "selectRoomReviewCnt", rrDTO);
        } finally {
            if (ss != null) { ss.close(); }
        }
        return totalCnt;
    }

    // 관리자 리뷰 목록 조회(페이징 적용)
    public List<ReviewAdminDomain> selectReviewList(ReviewRangeDTO rrDTO) throws SQLException {
        List<ReviewAdminDomain> list = null;
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            list = ss.selectList(NS + "selectReviewList", rrDTO);
        } finally {
            if (ss != null) { ss.close(); }
        }
        return list;
    }

    // roomTypeNum으로 필터된 리뷰 목록(페이징 적용)
    public List<ReviewAdminDomain> selectReviewByRoom(ReviewRangeDTO rrDTO) throws SQLException {
        List<ReviewAdminDomain> list = null;
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            list = ss.selectList(NS + "selectReviewByRoom", rrDTO);
        } finally {
            if (ss != null) { ss.close(); }
        }
        return list;
    }

    // 리뷰 1건 상세 정보 조회
    public ReviewAdminDomain selectReviewDetail(int reviewNum) throws SQLException {
        ReviewAdminDomain raDomain = null;
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            raDomain = ss.selectOne(NS + "selectReviewDetail", reviewNum);
        } finally {
            if (ss != null) { ss.close(); }
        }
        return raDomain;
    }

    // 리뷰 답변 등록/수정
    public int updateReplyReview(ReviewAdminDTO raDTO) throws SQLException {
        int cnt = 0;
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            cnt = ss.update(NS + "updateReplyReview", raDTO);
        } finally {
            if (ss != null) { ss.close(); }
        }
        return cnt;
    }

    // 해당 리뷰 삭제(soft delete)
    public int deleteAdminReview(int reviewNum) throws SQLException {
        int cnt = 0;
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            cnt = ss.update(NS + "deleteAdminReview", reviewNum);
        } finally {
            if (ss != null) { ss.close(); }
        }
        return cnt;
    }

    // 답변만 삭제
    public int deleteOnlyReply(int reviewNum) throws SQLException {
        int cnt = 0;
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            cnt = ss.update(NS + "deleteOnlyReply", reviewNum);
        } finally {
            if (ss != null) { ss.close(); }
        }
        return cnt;
    }

    // 리뷰 이미지 리스트 조회 (review_img 테이블)
    public List<String> selectReviewImgList(int reviewNum) throws SQLException {
        List<String> imgList = null;
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            imgList = ss.selectList(NS + "selectReviewImgList", reviewNum);
        } finally {
            if (ss != null) { ss.close(); }
        }
        return imgList;
    }

}
