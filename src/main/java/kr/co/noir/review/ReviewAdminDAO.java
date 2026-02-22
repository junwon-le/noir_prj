package kr.co.noir.review;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository("reviewAdminDAO")
public class ReviewAdminDAO {

    private static final String NS = "kr.co.noir.review.admin.ReviewAdminMapper.";

    /* =========================
       1. 전체 리뷰 수
       ========================= */
    public int selectReviewTotalCnt(ReviewRangeDTO rrDTO) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            return ss.selectOne(NS + "selectReviewTotalCnt", rrDTO);
        } finally {
            ss.close();
        }
    }

    /* =========================
       2. 객실 필터 리뷰 수
       ========================= */
    public int selectRoomReviewCnt(ReviewRangeDTO rrDTO) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            return ss.selectOne(NS + "selectRoomReviewCnt", rrDTO);
        } finally {
            ss.close();
        }
    }

    /* =========================
       3. 목록 조회
       ========================= */
    public List<ReviewAdminDomain> selectReviewList(ReviewRangeDTO rrDTO) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            return ss.selectList(NS + "selectReviewList", rrDTO);
        } finally {
            ss.close();
        }
    }

    public List<ReviewAdminDomain> selectReviewByRoom(ReviewRangeDTO rrDTO) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            return ss.selectList(NS + "selectReviewByRoom", rrDTO);
        } finally {
            ss.close();
        }
    }

    /* =========================
       4. 상세 조회
       ========================= */
    public ReviewAdminDomain selectReviewDetail(int reviewNum) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            return ss.selectOne(NS + "selectReviewDetail", reviewNum);
        } finally {
            ss.close();
        }
    }

    /* =========================
       5. 답변 등록/수정
       ========================= */
    public int updateReplyReview(ReviewAdminDTO raDTO) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            int cnt = ss.update(NS + "updateReplyReview", raDTO);
            ss.commit();  // 🔥 반드시 필요
            return cnt;
        } finally {
            ss.close();
        }
    }

    /* =========================
       6. 리뷰 soft delete
       ========================= */
    public int deleteAdminReview(int reviewNum) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            int cnt = ss.update(NS + "deleteAdminReview", reviewNum);
            ss.commit();  // 🔥 반드시 필요
            return cnt;
        } finally {
            ss.close();
        }
    }

    /* =========================
       7. 답변만 삭제
       ========================= */
    public int deleteOnlyReply(int reviewNum) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            int cnt = ss.update(NS + "deleteOnlyReply", reviewNum);
            ss.commit();  // 🔥 반드시 필요
            return cnt;
        } finally {
            ss.close();
        }
    }

    /* =========================
       8. 이미지 조회
       ========================= */
    public List<String> selectReviewImgList(int reviewNum) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            return ss.selectList(NS + "selectReviewImgList", reviewNum);
        } finally {
            ss.close();
        }
    }
}