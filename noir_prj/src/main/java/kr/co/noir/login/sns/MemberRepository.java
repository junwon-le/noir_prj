package kr.co.noir.login.sns;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MEMBER 테이블에 접근하기 위한 JPA 리포지토리입니다.
 * JpaRepository<엔티티, PK타입>을 상속받으면 기본적인 CRUD 메서드가 자동 생성됩니다.
 */
@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {

    /**
     * 1. SNS 로그인 시 사용: 가입된 SNS 제공자와 해당 고유 ID로 회원을 찾음
     * 쿼리: SELECT * FROM MEMBER WHERE MEMBER_PROVIDER = ? AND MEMBER_PROVIDER_ID = ?
     */
    Optional<MemberEntity> findByMemberProviderAndMemberProviderId(String memberProvider, String memberProviderId);

    /**
     * 2. 이메일로 회원 찾기: 중복 가입 체크나 비밀번호 찾기 등에 사용됨
     * 쿼리: SELECT * FROM MEMBER WHERE MEMBER_EMAIL = ?
     */
    Optional<MemberEntity> findByMemberEmail(String memberEmail);

    /**
     * 3. 일반 로그인 ID로 회원 찾기
     * 쿼리: SELECT * FROM MEMBER WHERE MEMBER_ID = ?
     */
    Optional<MemberEntity> findByMemberId(String memberId);

    /**
     * 4. 탈퇴하지 않은 회원 중 특정 ID로 찾기 (예시)
     * 쿼리: SELECT * FROM MEMBER WHERE MEMBER_ID = ? AND MEMBER_DEL_FLAG = 'N'
     */
    Optional<MemberEntity> findByMemberIdAndMemberDelFlag(String memberId, String memberDelFlag);
}