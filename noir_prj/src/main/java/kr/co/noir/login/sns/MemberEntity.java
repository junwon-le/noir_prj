package kr.co.noir.login.sns;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "MEMBER") // 실제 DB 테이블명과 매핑
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 무분별한 생성을 막기 위해 PROTECTED 설정
@AllArgsConstructor
@Builder
@ToString(exclude = "memberPass") // 보안상 비밀번호는 toString에서 제외
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 
    // @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ") // Oracle의 경우
    @Column(name = "MEMBER_NUM")
    private Integer memberNum; // DTO의 int를 Integer로 변경 (null 처리가능하여 JPA에서 권장)

    @Column(name = "MEMBER_ID", unique = true, length = 50)
    private String memberId;

    @Column(name = "MEMBER_PASS", nullable = false)
    private String memberPass;

    @Column(name = "MEMBER_LAST_NAME")
    private String memberLastName;

    @Column(name = "MEMBER_FIRST_NAME")
    private String memberFirstName;

    @Column(name = "MEMBER_EMAIL", length = 100)
    private String memberEmail;

    @Column(name = "MEMBER_TEL", length = 20)
    private String memberTel;

    @Column(name = "MEMBER_BIRTH", length = 10)
    private String memberBirth;

    @Column(name = "MEMBER_DEL_FLAG", length = 1)
    @Builder.Default
    private String memberDelFlag = "N"; // 기본값 'N' 설정

    @Column(name = "MEMBER_INPUTDATE", updatable = false)
    private String memberInputDate; // 기존 관례에 따라 String 유지 (LocalDateTime 권장)

    @Column(name = "MEMBER_IP")
    private String memberIp;

    // SNS 로그인 정보 추가 매핑
    @Column(name = "MEMBER_PROVIDER")
    private String memberProvider; // google, kakao, naver 등

    @Column(name = "MEMBER_PROVIDER_ID")
    private String memberProviderId; // SNS에서 주는 고유 고유번호

    /* -----------------------------------------------------------
     * 비즈니스 로직 (엔티티 내에서 상태를 변경하는 메서드)
     * ----------------------------------------------------------- */

    /**
     * 회원 탈퇴 처리
     */
    public void markAsWithdrawn() {
        this.memberDelFlag = "Y";
    }

    /**
     * SNS 정보 업데이트 (재로그인 시 정보 갱신 등)
     */
    public void updateSnsInfo(String email, String lastName, String firstName) {
        this.memberEmail = email;
        this.memberLastName = lastName;
        this.memberFirstName = firstName;
    }
}