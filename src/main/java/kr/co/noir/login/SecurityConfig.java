package kr.co.noir.login;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;


    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. 세션 관리: 응답 커밋 전 세션 생성을 보장하여 타임리프 에러 방지
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) 
            ) // 
            .csrf(csrf -> csrf
                // [핵심] /reserve/ 하위의 POST 요청에 대해 CSRF 검사를 생략합니다.
                .ignoringRequestMatchers("/reserve/**", "/loginProcess", "/dinning/**")
            )
            // 2. 인가 설정: 허용할 경로를 구체적으로 지정
            .authorizeHttpRequests(auth -> auth
                // "/**"는 모든 보안을 해제하므로 제거하고, 필요한 경로만 허용합니다.
                .requestMatchers("/**", "/login/**", "/reserve/**", "/login/**", "/oauth2/**", "/error").permitAll()
                .requestMatchers("/common/**", "/images/**", "/css/**", "/js/**").permitAll()
                .requestMatchers("/adminLogin", "/adminLogout").permitAll()
                
                // 그 외 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            
            // 3. OAuth2 로그인 설정
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login/memberLogin") // [수정] 사용자님의 실제 로그인 페이지 경로
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(customOAuth2UserService) 
                )
                .defaultSuccessUrl("/", true)
                
             // 4. 여기서 .failureHandler()를 호출
                .failureHandler(oAuth2LoginFailureHandler)
                
            )
            
            // 4. 로그아웃 설정
            .logout(logout -> logout
                .logoutSuccessUrl("/") 
                .invalidateHttpSession(true) 
                .deleteCookies("JSESSIONID")
            );
        

        return http.build();
    }
    
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()) 
            .requestMatchers("/favicon.ico", "/resources/**", "/error"); 
    }
}