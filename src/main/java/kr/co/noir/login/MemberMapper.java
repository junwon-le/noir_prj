package kr.co.noir.login;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper { 
    
	LoginMemberDomain selectOneUserInfo(String memberId);
    
    // 로그인: 아이디와 비밀번호로 회원 조회
    MemberDTO login(@Param("memberId") String memberId, @Param("memberPass") String memberPass);

    // 아이디 찾기: 성, 이름, 이메일로 아이디 조회 (이미지 컬럼 반영)
    String findIdByInfo(@Param("memberLastName") String memberLastName, 
                          @Param("memberFirstName") String memberFirstName,
                          @Param("memberEmail") String memberEmail);

    /**
     * 비밀번호 재설정 전 사용자 정보 일치 여부 확인
     */
    // 	리턴타입      메서드명(매개변수들...)
    MemberDTO checkUserForReset(@Param("memberId") String memberId, 
                                        @Param("memberLastName") String memberLastName, 
                                        @Param("memberFirstName") String memberFirstName);
    
    // 비밀번호 변경: 아이디를 기준으로 새 비밀번호 업데이트
    int updatePassword(@Param("memberId") String memberId, @Param("newPw") String newPw);

    // 아이디 중복 체크: MEMBER_ID 카운트 조회
    int selectIdCount(@Param("memberId") String memberId);

    // 회원가입: DTO에 담긴 정보를 MEMBER 테이블에 삽입 (이미지 컬럼 전체 반영)
    int insertMember(MemberDTO memberDTO);
    
    
    
}