package kr.co.noir.login;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private MemberMapper memberMapper; // 🔍 의존성 주입 확인

    @Value("${user.crypto.key}")
    private String key;
    @Value("${user.crypto.salt}")
    private String salt;    
    
    @Autowired
    private HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // 1. 제공자 정보 및 Access Token 추출
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google, kakao 등
        String provider = registrationId.toUpperCase(); 
        String accessToken = userRequest.getAccessToken().getTokenValue();
        
        // userRequest에서 RefreshToken은 직접 가져올 수 없으므로 우선 null 처리
        String refreshToken = null; 

        // 2. 각 SNS별 속성 매핑 (이전에 만든 OAuthAttributes 활용)
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // 3. 회원 저장 및 정보 가져오기 (매개변수 타입 일치시킴)
        // DB에서 회원 정보를 가져오거나 가입시킴
        MemberDTO member = saveOrUpdate(attributes); 
        
        // UI 출력용 성과 이름을 합쳐서 저장, 세션에 이름 및 회원 정보 저장 
        String fullName = (member.getMemberLastName() != null ? member.getMemberLastName() : "") 
                        + (member.getMemberFirstName() != null ? member.getMemberFirstName() : "");
        
        httpSession.setAttribute("memberId", member.getMemberId());   // 헤더의 th:if 조건을 충족
        httpSession.setAttribute("memberName", fullName);           // 이름 표시용
        httpSession.setAttribute("memberNum", member.getMemberNum()); // PK 값
        httpSession.setAttribute("loginUser", member);              // 객체 전체        
        
        // 4. 토큰 암호화 및 DB 저장 (Upsert)
        TextEncryptor te = Encryptors.text(key, salt);
        
        // 만료 시간 계산 (현재 시간 + 유효 초)
        java.time.LocalDateTime expiresAt = java.time.LocalDateTime.now().plusSeconds(
            userRequest.getAccessToken().getExpiresAt().getEpochSecond() - java.time.Instant.now().getEpochSecond()
        );

        // SnsTokenDTO 객체 생성 및 실제 Mapper 호출 [이 부분이 빠져있었습니다]
        SnsTokenDTO tokenDTO = SnsTokenDTO.builder()
                .memberNum(member.getMemberNum())
                .provider(provider)
                .accessToken(te.encrypt(accessToken))
                .refreshToken(refreshToken != null ? te.encrypt(refreshToken) : null)
                .tokenExpiresAt(expiresAt)
                .build();

        // 실제 DB 저장 실행!
        memberMapper.upsertSnsToken(tokenDTO);
        
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
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