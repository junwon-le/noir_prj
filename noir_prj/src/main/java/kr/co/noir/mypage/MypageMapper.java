package kr.co.noir.mypage; // 패키지명 확인 필수
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MypageMapper {
    
    // 1. 비밀번호 조회
    String selectPasswordById(String memberId);

    // 2. 탈퇴 처리 (삭제 플래그 업데이트)
    void updateMemberDelFlag(String memberId);
    void deleteSnsToken(String memberId, String provider);
    

}