package kr.co.noir.interceptor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class UserInterceptorConfig implements WebMvcConfigurer {
	
	@Autowired
	private UserInterceptor userInterceptor;
	
	@Value("${user.addPath}")
	private List<String> addPath;
	
	@Value("${user.excludePath}")
	private List<String> excludePath;
	

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(userInterceptor)
		.addPathPatterns(addPath)
		.excludePathPatterns(excludePath);
		
	}
}
