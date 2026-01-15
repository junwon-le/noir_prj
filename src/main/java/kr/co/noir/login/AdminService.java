package kr.co.noir.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminService {

	@Autowired
	private AdminMapper adminMapper;
	
	public AdminDTO login(String adminId, String adminPass) {
		return adminMapper.login(adminId, adminPass);
	}
}



