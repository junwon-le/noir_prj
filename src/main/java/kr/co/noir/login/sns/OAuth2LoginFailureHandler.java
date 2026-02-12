package kr.co.noir.login.sns;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {
	         
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
    	
        // 1. OAuth2AuthenticationException인지 확인
        if (exception instanceof OAuth2AuthenticationException) {
            String errorCode = ((OAuth2AuthenticationException) exception).getError().getErrorCode();
            
         // 2. 탈퇴한 회원인 경우
            if ("withdrawn_member".equals(errorCode)) {
                String message = URLEncoder.encode("탈퇴한 계정입니다. 재가입하시겠습니까?", StandardCharsets.UTF_8);
                
                // [핵심 추가] 어떤 SNS인지 확인 (URI 예시: /login/oauth2/code/naver)
                String provider = "kakao"; // 기본값
                String uri = request.getRequestURI();
                
                if (uri.contains("naver")) {
                    provider = "naver";
                } else if (uri.contains("google")) {
                    provider = "google";
                } else if (uri.contains("kakao")) {
                    provider = "kakao";
                }
                
                // 리다이렉트 URL에 provider 파라미터 추가
                response.sendRedirect("/login/memberLogin?error=withdrawn&message=" + message + "&provider=" + provider);
                return;
            }
        }

        // 3. 그 외의 일반적인 로그인 실패 처리
        response.sendRedirect("/login?error=true");
    }
}