package kr.co.noir.interceptor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import io.micrometer.common.lang.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserInterceptor  implements HandlerInterceptor {

	@Value("${user.loginFrm}")
	private String loginFrmURL;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
	
			boolean flag=false;
		
			HttpSession session=request.getSession();
			Object obj=session.getAttribute("memberId");
			
			flag= obj!=null; //로그인 되었는지?
			
			if( !flag ) { //로그인이 되어 있지 않은 상황 
				response.sendRedirect(loginFrmURL);
			}//end if
		    
			return flag;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable ModelAndView modelAndView) throws Exception {
		System.out.println("postHandle : 컨트롤러 실행 후 View가 랜더링 되기 전 실행 ");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		System.out.println("afterCompletion : 요청 완료 후 실행 ");
	}

	
	
}//class
