package kr.co.noir.login;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NonMemberMapper { 
	    List<NonMemberDTO> selectNonMemberList(Map<String, Object> params);
	    int selectTotalCount(Map<String, Object> params);
	    int deleteNonMembers(List<Integer> ids);
}// class
