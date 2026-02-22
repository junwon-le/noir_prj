package kr.co.noir.mypageReserve;

import java.util.ArrayList;
import java.util.List;
import kr.co.noir.login.LoginService;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository
public class MypageReserveDAO {

    private final LoginService loginService;


    MypageReserveDAO(LoginService loginService) {
        this.loginService = loginService;
    }
	
	
	
	public int selectTotalCnt(ReserveSearchDTO rsDTO) throws PersistenceException{
		int totalCnt=0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		totalCnt=ss.selectOne("kr.co.noir.mypageReserve.selectTotalCnt",rsDTO);
		if(ss !=null) {ss.close();}//end if
		System.out.println(totalCnt);
		return totalCnt;
		
	}//selectTotalCnt

	public List<HotelRevSearchDomain> selectHotelRevList(ReserveSearchDTO rsDTO) throws PersistenceException{
		List<HotelRevSearchDomain> list = new ArrayList<HotelRevSearchDomain>();
		SqlSession ss= MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		list=ss.selectList("kr.co.noir.mypageReserve.hotelRevList",rsDTO);
		
		if(ss !=null) {ss.close();}//end if
		
		return list;
		
	}//selectHotelRevList
	
	public List<HotelRevDetailDomain> selectHotelRevDetail(ReserveDetailDTO rdDTO) throws PersistenceException{
		List<HotelRevDetailDomain> hrdDomain=null;
		
		SqlSession ss= MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		hrdDomain=ss.selectList("kr.co.noir.mypageReserve.hotelRevDetail",rdDTO);
		
		if(ss !=null) {ss.close();}//end if
		
		System.out.println(hrdDomain);
		
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
		
		System.out.println("revCnt---"+revCnt);
		System.out.println("payCnt---"+payCnt);
		
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
	}//selectHotelRevDetail
	
	
//=================================다이닝 리스트 출력=======================
	
	public List<DinningRevSearchDomain> selectDinningRevList(ReserveSearchDTO rsDTO) throws PersistenceException{
		List<DinningRevSearchDomain> list = null;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		list=ss.selectList("kr.co.noir.mypageReserve.dinningRevList",rsDTO);
		
		System.err.println(list);
		
		if(ss!=null) {ss.close();}//end if
		
		return list;

	}//selectDinningRevList
	
	
	public DinningRevDetailDomain selectDinningRevDetail(ReserveDetailDTO rdDTO) throws PersistenceException{
		DinningRevDetailDomain drdDomain=null;
		
		SqlSession ss= MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		drdDomain=ss.selectOne("kr.co.noir.mypageReserve.dinningRevDetail",rdDTO);
		
		if(ss !=null) {ss.close();}//end if
		
		
		return drdDomain;
		
		
	}//selectHotelRevDetail
	
	
	public boolean updateDinningReserve(int reserveNum)throws PersistenceException{
		
		boolean flag = false;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		int revCnt=ss.update("kr.co.noir.mypageReserve.removeHotelReserve",reserveNum);
		int payCnt=ss.update("kr.co.noir.mypageReserve.removeRevPay",reserveNum);
		
		if ((revCnt+payCnt)==2) { 
            ss.commit();
            System.out.println("DB 커밋 성공!");
            flag=true;
        } else {
            ss.rollback();
            System.out.println("조건 미달로 인한 롤백");
        }//end else
		
		return flag;
	}//updateDinningReserve
	
	
}//class
