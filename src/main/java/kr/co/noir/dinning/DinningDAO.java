package kr.co.noir.dinning;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import kr.co.noir.dao.MyBatisHandler;

@Repository
public class DinningDAO {

	public DinningDomain selectDinning() throws PersistenceException{
		
		DinningDomain dDomain = null;
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		dDomain = ss.selectOne("kr.co.noir.dinning.dinningSelect");
		if(ss!=null) {
			ss.close();
		}
		return dDomain;
	}
	
	public List<DinningDomain> selectDetailDinning() throws PersistenceException{
		List<DinningDomain> list = new ArrayList<DinningDomain>();
		
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(true);
		list = ss.selectList("kr.co.noir.dinning.dinningDetailSelect");
		
		if(ss!=null) {
			ss.close();
		}

		return list;
	}
	
	public int updateDinning(DinningDTO dDTO) throws PersistenceException{
		int cnt = 0;
		SqlSession ss = MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		cnt+= ss.update("kr.co.noir.dinning.infoUpdate",dDTO);		
		cnt+= ss.update("kr.co.noir.dinning.morningMenuUpdate",dDTO);		
		cnt+= ss.update("kr.co.noir.dinning.lunchMenuUpdate",dDTO);		
		cnt+= ss.update("kr.co.noir.dinning.dinnerMenuUpdate",dDTO);		
		cnt+= ss.update("kr.co.noir.dinning.timeUpdate",dDTO);
		cnt+= ss.update("kr.co.noir.dinning.morningMenuPrice",dDTO);		
		cnt+= ss.update("kr.co.noir.dinning.lunchMenuPrice",dDTO);		
		cnt+= ss.update("kr.co.noir.dinning.dinnerMenuPrice",dDTO);

		if(cnt==8) {
			ss.commit();
		}else {
			ss.rollback();
		}
				
		if(ss!=null) {
			ss.close();
		}

		
		return cnt;
	}
	
}
