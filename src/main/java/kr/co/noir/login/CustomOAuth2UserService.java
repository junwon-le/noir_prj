package kr.co.noir.login;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberMapper memberMapper; // 🔍 의존성 주입 확인
    private final HttpSession httpSession;
    private final MemberRepository memberRepository;  // Member 테이블 접근용

    @Value("${user.crypto.key}")
    private String key;
    @Value("${user.crypto.salt}")
    private String salt;    


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 1. SNS에서 사용자 정보 및 Access Token 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google, kakao 등
        String provider = registrationId.toUpperCase(); 
        String accessToken = userRequest.getAccessToken().getTokenValue();

        String providerId=registrationId;
        
        
        // userRequest에서 RefreshToken은 직접 가져올 수 없으므로 우선 null 처리
        String refreshToken = null; 

        // 2. 각 SNS별 속성 매핑 (이전에 만든 OAuthAttributes 활용)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 3. DB 조회 및 탈퇴 여부 체크 
        // DB에서 회원 정보를 가져와서 탈퇴훠원이라면 로그인하면 안됨 
        MemberEntity member = memberRepository
        		.findByMemberProviderAndMemberProviderId(attributes.getProvider(),attributes.getProviderId())
                .orElse(null);
        
        if (member != null && "Y".equals(member.getMemberDelFlag() )) {

        		// 1. 세션에서 "재가입 모드"인지 확인
            Boolean isRejoinMode = (Boolean) httpSession.getAttribute("IS_REJOIN_MODE");
            MemberDTO memberReionDTO = memberMapper.findByProviderAndId(provider, registrationId);    

            if (isRejoinMode != null && isRejoinMode) {
                // [재가입 처리]
                // A. 탈퇴 플래그를 'N'으로 복구 (DB 업데이트)
            		memberMapper.updateMemberRejoin(registrationId);
            		// MERGE SQL 실행! (복구됨)
                memberMapper.insertSnsMember(memberReionDTO);            		
                
                // B. 세션 표식 제거 (일회용이므로)
                httpSession.removeAttribute("IS_REJOIN_MODE");
                
                // C. 로그인 계속 진행 (성공!)
//                return new PrincipalDetails(member, oAuth2User.getAttributes());
                
            } else {
                // [일반 로그인 시도] -> 예외 발생시켜서 모달 띄우기
                throw new OAuth2AuthenticationException(new OAuth2Error("withdrawn_member"), "탈퇴한 회원입니다.");
            }

        }

        
        MemberDTO memberDTO = saveOrUpdate(attributes); 
        
        
        // 기존 세션 무효화 및 새 세션 발급
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        
        // 기존 세션이 있다면 완전히 파기
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        
        // 새 세션 생성 (사용자 신분으로 새로 발급)
        HttpSession nSession = request.getSession(true);        
        
        
        // UI 출력용 성+이름을 합쳐서 저장, 세션에 이름 및 회원 정보 저장 
        String fullName = (memberDTO.getMemberLastName() != null ? memberDTO.getMemberLastName() : "") 
                        + (memberDTO.getMemberFirstName() != null ? memberDTO.getMemberFirstName() : "");
        
        nSession.setAttribute("memberId", memberDTO.getMemberId());   // 헤더의 th:if 조건을 충족
        nSession.setAttribute("memberName", fullName);           // 이름 표시용
        nSession.setAttribute("memberNum", memberDTO.getMemberNum()); // PK 값
        nSession.setAttribute("loginUser", memberDTO);              // 객체 전체        
        nSession.setAttribute("memberProvider", memberDTO.getMemberProvider()); // 
        nSession.setAttribute("memberProviderId", memberDTO.getMemberProviderId()); // 
        
//        System.out.println("SNS login : "+ fullName);
        
        // 4. 토큰 암호화 및 DB 저장 (Upsert)
        TextEncryptor te = Encryptors.text(key, salt);
        
        // 만료 시간 계산 (현재 시간 + 유효 초)
        java.time.LocalDateTime expiresAt = java.time.LocalDateTime.now().plusSeconds(
            userRequest.getAccessToken().getExpiresAt().getEpochSecond() - java.time.Instant.now().getEpochSecond()
        );

        // SnsTokenDTO 객체 생성 및 실제 Mapper 호출
        // member가 null일 수 있으므로 memberDTO에서 번호를 가져옴
        SnsTokenDTO tokenDTO = SnsTokenDTO.builder()
                .memberNum(memberDTO.getMemberNum()) 
                .provider(provider)
                .accessToken(te.encrypt(accessToken))
                .refreshToken(refreshToken != null ? te.encrypt(refreshToken) : null)
                .tokenExpiresAt(expiresAt)
                .build();

        // 실제 DB 저장 실행!
        // 5. 정상 회원이면 OAuth2User 반환 (로그인 진행)
        memberMapper.updateSnsToken(tokenDTO);
        
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
        
//        return oAuth2User;        
    }

    private MemberDTO saveOrUpdate(OAuthAttributes attributes) {
        MemberDTO member = memberMapper.findByProviderAndId(attributes.getProvider(), attributes.getProviderId());

        if (member == null) {
            member = MemberDTO.builder()
                    .memberId(attributes.getProvider().toLowerCase() + "_" + attributes.getProviderId())
                    .memberPass("SNS_USER")
                    .memberFirstName(attributes.getName()) // SNS에서 가져온 이름
                    .memberEmail(attributes.getEmail())
                    .memberProvider(attributes.getProvider())
                    .memberProviderId(attributes.getProviderId())
                    .memberLastName("") // 성은 빈값 처리
                    .build();
            memberMapper.insertSnsMember(member);
            
            // [중요] 방금 가입한 회원의 memberNum 등 정보를 DB에서 다시 확실히 가져옵니다.
            member = memberMapper.findByProviderAndId(attributes.getProvider(), attributes.getProviderId());
        }
        return member;
    }
}