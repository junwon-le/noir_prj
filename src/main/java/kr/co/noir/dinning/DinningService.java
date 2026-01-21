package kr.co.noir.dinning;

import java.util.List;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DinningService {
	
	@Autowired
	private DinningDAO dDAO;
	
	
	public DinningDomain searchDinning() {
		DinningDomain dDomain = null;
		try {
			dDomain = dDAO.selectDinning();
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
		
		return dDomain; 
	}
	
	public List<DinningDomain> searchDetailDinning() {
		List<DinningDomain> list = null;
		try {
			list = dDAO.selectDetailDinning();
		}catch(PersistenceException pe) {
			pe.printStackTrace();
		}
		
		return list; 
	}
	

	
}
