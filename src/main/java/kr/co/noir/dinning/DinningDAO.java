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
	
}
