package kr.co.noir.inquiry;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository("inquiryAdminDAO")
public class InquiryAdminDAO {
	
	
    // 총 문의 개수 (del_flag='N' 기준, 검색 포함)
	public int selectinquiryTotal( InquiryRangeDTO irDTO) throws SQLException {
        int totalCnt = 0;

        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
        totalCnt = ss.selectOne("kr.co.noir.inquiry.selectInquiryTotalCnt", irDTO);

        if (ss != null) { ss.close(); }
        return totalCnt;
	}//selectinquiryTotal
	
	
    // 문의 목록(상단 테이블)
	public List<InquiryAdminDomain> selectinquiryList( InquiryRangeDTO irDTO ) throws SQLException{
        List<InquiryAdminDomain> list = null;

        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        list = ss.selectList("kr.co.noir.inquiry.selectInquiryList", irDTO);

        if (ss != null) { ss.close(); }
        return list;
	}//selectinquiryList
	
    // 문의 상세(하단 영역)
	public InquiryAdminDomain selectInquiryDetail( int inquiryNum ) throws SQLException {
		InquiryAdminDomain detail = null;

        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        detail = ss.selectOne("kr.co.noir.inquiry.selectInquiryDetail", inquiryNum);

        if (ss != null) { ss.close(); }
        return detail;
	}//selectInquiryDetail

	
    // 답변 등록 / 수정
	public int updateinquiry(InquiryAdminDTO iaDTO) throws SQLException {
        int cnt = 0;

        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        cnt = ss.update("kr.co.noir.inquiry.updateInquiryReturn", iaDTO);

        if (ss != null) { ss.close(); }
        return cnt;
	}
	 
	 // 삭제
	public int deleteinquiry(int inquiryNum) throws SQLException {

        int cnt = 0;

        SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
        cnt = ss.update("kr.co.noir.inquiry.updateInquiryDelFlag", inquiryNum);

        if (ss != null) { ss.close(); }
        return cnt;
	}
	
}//class
