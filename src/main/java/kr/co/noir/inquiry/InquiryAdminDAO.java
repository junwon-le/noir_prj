package kr.co.noir.inquiry;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository("inquiryAdminDAO")
public class InquiryAdminDAO {

    private static final String NS = "kr.co.noir.inquiryAdmin."; // ✅ 관리자 namespace로 분리 권장

    // 총 문의 개수
    public int selectInquiryTotal(InquiryRangeDTO irDTO) throws SQLException {
        int totalCnt = 0;

        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        totalCnt = ss.selectOne(NS + "selectInquiryTotalCnt", irDTO);

        if (ss != null) { ss.close(); }
        return totalCnt;
    }

    // 문의 목록(상단 테이블)
    public List<InquiryAdminDomain> selectInquiryList(InquiryRangeDTO irDTO) throws SQLException {
        List<InquiryAdminDomain> list = null;

        // ✅ 조회는 false
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        list = ss.selectList(NS + "selectInquiryList", irDTO);

        if (ss != null) { ss.close(); }
        return list;
    }

    // 문의 상세(하단 영역)
    public InquiryAdminDomain selectInquiryDetail(int inquiryNum) throws SQLException {
        InquiryAdminDomain detail = null;

        // ✅ 조회는 false
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        detail = ss.selectOne(NS + "selectInquiryDetail", inquiryNum);

        if (ss != null) { ss.close(); }
        return detail;
    }

    // 답변 등록 / 수정
    public int updateInquiry(InquiryAdminDTO iaDTO) throws SQLException {
        int cnt = 0;

        // ✅ 쓰기는 true
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        cnt = ss.update(NS + "updateInquiryReturn", iaDTO);

        if (ss != null) { ss.close(); }
        return cnt;
    }

    // 삭제(논리삭제)
    public int deleteInquiry(int inquiryNum) throws SQLException {
        int cnt = 0;

        // ✅ 쓰기는 true
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        cnt = ss.update(NS + "updateInquiryDelFlag", inquiryNum);

        if (ss != null) { ss.close(); }
        return cnt;
    }
    
 // ✅ adminId로 admin_num 조회 (FK 만족용)
    public Integer selectAdminNumByAdminId(String adminId) throws SQLException {
        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        Integer adminNum = ss.selectOne(NS + "selectAdminNumByAdminId", adminId);

        if (ss != null) { ss.close(); }
        return adminNum;
    }
}