package kr.co.noir.login;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // SNS 정보로 객체를 쉽게 생성하기 위해 추가
public class MemberDTO {
    // 1. 기본 정보 (DB 컬럼과 1:1 매핑)
    private int memberNum;          // MEMBER_NUM (PK)
    private String memberId;        // MEMBER_ID (SNS 유저는 이메일 등으로 대체)
    private String memberPass;      // MEMBER_PASS (SNS 유저는 임의의 값 저장)
    private String memberLastName;  // MEMBER_LAST_NAME (성)
    private String memberFirstName; // MEMBER_FIRST_NAME (이름)
    private String memberEmail;     // MEMBER_EMAIL
    private String memberTel;       // MEMBER_TEL
    private String memberBirth;     // MEMBER_BIRTH
    private String memberDelFlag;   // MEMBER_DEL_FLAG ('N' or 'Y')
    private String memberInputDate; // MEMBER_INPUTDATE (가입일)
    private String memberIp;        // MEMBER_IP

    // 2. SNS 로그인 정보 (추가된 필드)
    private String memberProvider;   // MEMBER_PROVIDER (google, kakao, naver 등)
    private String memberProviderId; // MEMBER_PROVIDER_ID (SNS 고유 식별자)

    // 3. 비즈니스 로직 및 조회를 위한 필드
    private String memberName;       // 성 + 이름 결합용 가상 필드
    private List<Integer> selectedMembers; // 탈퇴/삭제 처리용 리스트
    
    // 추가: 성과 이름을 합쳐주는 메서드 (조회 시 편리함)
    public String getMemberName() {
        if (memberLastName != null && memberFirstName != null) {
            return memberLastName + memberFirstName;
        }
        return memberFirstName; // 성이 없는 경우 이름만 반환
    }
    
}