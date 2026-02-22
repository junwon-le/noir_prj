package kr.co.noir.notice;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository("noticeAdminDAO")
public class NoticeAdminDAO {

    public int selectNoticeTotalCnt(BoardRangeDTO rDTO) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            return ss.selectOne("kr.co.noir.noticeAdmin.selectNoticeTotalCnt", rDTO);
        } finally {
            if (ss != null) ss.close();
        }
    }

    public List<NoticeAdminDomain> selectNoticeList(BoardRangeDTO rDTO) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            return ss.selectList("kr.co.noir.noticeAdmin.selectNoticeList", rDTO);
        } finally {
            if (ss != null) ss.close();
        }
    }

    public NoticeAdminDomain selectNoticeDetail(int noticeNum) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            return ss.selectOne("kr.co.noir.noticeAdmin.selectNoticeDetail", noticeNum);
        } finally {
            if (ss != null) ss.close();
        }
    }

    public int insertNotice(NoticeAdminDTO naDTO) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        try {
            return ss.insert("kr.co.noir.noticeAdmin.insertNotice", naDTO);
        } finally {
            if (ss != null) ss.close();
        }
    }

    public int updateNotice(NoticeAdminDTO naDTO) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        try {
            return ss.update("kr.co.noir.noticeAdmin.updateNotice", naDTO);
        } finally {
            if (ss != null) ss.close();
        }
    }

    public int deleteNotice(int noticeNum) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        try {
            return ss.update("kr.co.noir.noticeAdmin.deleteNotice", noticeNum);
        } finally {
            if (ss != null) ss.close();
        }
    }

    /* =========================================================
       ✅ 추가: adminId로 adminNum 조회 (세션 보정용)
       - 컨트롤러/서비스에서 adminNum 세션이 비었을 때 사용
       - mapper.xml에 selectAdminNumByAdminId가 반드시 있어야 함
       ========================================================= */
    public Integer selectAdminNumByAdminId(String adminId) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        try {
            return ss.selectOne("kr.co.noir.noticeAdmin.selectAdminNumByAdminId", adminId);
        } finally {
            if (ss != null) ss.close();
        }
    }
}