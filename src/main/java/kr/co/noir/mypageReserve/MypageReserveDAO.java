package kr.co.noir.mypageReserve;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository
public class MypageReserveDAO {
	
	
	
	public int selectTotalCnt(ReserveSearchDTO rsDTO) throws PersistenceException{
		int totalCnt=0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		totalCnt=ss.selectOne("kr.co.noir.mypageReserve.selectTotalCnt",rsDTO);
		if(ss !=null) {ss.close();}//end if
		
		return totalCnt;
		
	}

	public List<HotelRevSearchDomain> selectHotelRevList(ReserveSearchDTO rsDTO) throws PersistenceException{
		List<HotelRevSearchDomain> list = new ArrayList<HotelRevSearchDomain>();
		SqlSession ss= MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		list=ss.selectList("kr.co.noir.mypageReserve.hotelRevList",rsDTO);
		
		if(ss !=null) {ss.close();}//end if
		
		return list;
		
	}//selectHotelRevList
	
	public HotelRevDetailDomain selectHotelRevDetail(ReserveDetailDTO rdDTO) throws PersistenceException{
		HotelRevDetailDomain hrdDomain=null;
		
		SqlSession ss= MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		hrdDomain=ss.selectOne("kr.co.noir.mypageReserve.hotelRevDetail",rdDTO);
		
		if(ss !=null) {ss.close();}//end if
		
		
		return hrdDomain;
		
		
	}//selectHotelRevDetail
	
	
	public int updateHotelReserve(int revNum) throws PersistenceException{
		
		int revCnt=0;
		int payCnt=0;
		int cnt=0;
		
		System.out.println("예약번호"+revNum);
		SqlSession ss= MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		revCnt=ss.update("kr.co.noir.mypageReserve.removeHotelReserve",revNum);
		payCnt=ss.update("kr.co.noir.mypageReserve.removeRevPay",revNum);
		
		cnt=revCnt+payCnt;
		if (cnt == 2) { 
            ss.commit();
            System.out.println("DB 커밋 성공!");
        } else {
            ss.rollback();
            System.out.println("조건 미달로 인한 롤백");
        }//end if
		
		if(ss !=null) {ss.close();}//end if
		
		
		return cnt;
	}
	
}
