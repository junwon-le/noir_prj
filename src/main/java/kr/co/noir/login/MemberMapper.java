package kr.co.noir.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper { 
	// 아이디와 비밀번호로 관리자 조회
	MemberDTO login(@Param("memberId") String memberId, @Param("memberPass") String memberPass);
	MemberDTO findIdByNameAndEmail(@Param("memberName") String memberName, @Param("memberEmail") String memberEmail);
}
