package kr.co.noir.login;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    @Autowired
    private HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        MemberDTO member = saveOrUpdate(attributes);
        
        // UI 출력을 위해 세션 데이터 설정
        httpSession.setAttribute("memberName", member.getMemberFirstName());
        httpSession.setAttribute("memberId", member.getMemberId());

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
                    .memberFirstName(attributes.getName())
                    .memberEmail(attributes.getEmail())
                    .memberProvider(attributes.getProvider())
                    .memberProviderId(attributes.getProviderId())
                    .memberLastName("")
                    .build();
            memberMapper.insertSnsMember(member);
        }
        return member;
    }
}