package kr.co.noir.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminMapper { 
	// 아이디와 비밀번호로 관리자 조회
	AdminDTO adminLogin(@Param("adminId") String adminId, @Param("adminPass") String adminPass);
}
