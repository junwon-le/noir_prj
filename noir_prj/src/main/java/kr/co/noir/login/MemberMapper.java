package kr.co.noir.login;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.noir.login.sns.SnsTokenDTO;

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
     * 리턴타입      메서드명(매개변수들...)
     */
    MemberDTO checkUserForReset(@Param("memberId") String memberId, 
                                        @Param("memberLastName") String memberLastName, 
                                        @Param("memberFirstName") String memberFirstName);
    
    // 비밀번호 변경: 아이디를 기준으로 새 비밀번호 업데이트
    int updatePassword(@Param("memberId") String memberId, @Param("newPw") String newPw);

    // 아이디 중복 체크: MEMBER_ID 카운트 조회
    int selectIdCount(@Param("memberId") String memberId);

    // 회원가입: DTO에 담긴 정보를 MEMBER 테이블에 삽입 (이미지 컬럼 전체 반영)
    int insertMember(MemberDTO memberDTO);
    
    // 검색 조건 및 페이징을 포함한 목록 조회
    List<MemberDTO> selectMemberList(Map<String, Object> params);
    
    // 전체 레코드 수 (페이징용)
    int selectTotalCount(Map<String, Object> params);
    
    // 선택된 회원들 탈퇴 처리 (MEMBER_DEL_FLAG = 'Y')
    int updateMembersWithdraw(@Param("memberIds") List<String> memberIds);    

    // 선택된 회원을 탈퇴 처리 (MEMBER_DEL_FLAG = 'Y')
    int updateMemberWithdraw(String memberId);    

    // 선택된 회원을 재가입 처리 (MEMBER_DEL_FLAG = 'N')
    int updateMemberRejoin(String memberId);    
    
    // SNS 가입 처리 
    // 기존 가입 여부 확인 (Provider와 ProviderId의 조합으로 조회)
    MemberDTO findByProviderAndId(@Param("provider") String provider, 
                                  @Param("providerId") String providerId);

    // 신규 SNS 유저 등록
    int insertSnsMember(MemberDTO member);    
    
    // 향후 SNS 탈퇴 처리를 위한 토큰 등록
    void updateSnsToken(SnsTokenDTO tokenDTO);
    
    // 매개변수가 2개 이상일 때는 반드시 이름을 지정해줘야 XML에서 인식한다.
    SnsTokenDTO selectSnsToken(@Param("memberId") String memberId, @Param("provider") String provider);

    void deleteSnsToken(@Param("memberId") String memberId, @Param("provider") String provider);

    // ID 찾기에 사용할 성과 이름으로 정보 검색
	List<MemberDTO> findMembersByName(String memberLastName, String memberFirstName);
    
}