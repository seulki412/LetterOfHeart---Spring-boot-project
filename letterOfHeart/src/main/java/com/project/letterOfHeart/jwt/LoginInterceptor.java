package com.project.letterOfHeart.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI = request.getRequestURI();
		System.out.println("[interceptor] requestURI : " + requestURI);
		
		if( "error".equals(requestURI) ) {
			response.sendRedirect("index") ;
			return false ;
		}
		
		return true; // false -> 이후에 진행을 하지 않는다.
	}
}

/*
@Service public class LoginCheckInterceptor extends HandlerInterceptorAdapter { 
	@Override public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession (true); 
		String user = (String) session.getAttribute("user");
		
		if("index.html".equals(request.getRequestURI() )) return true; //제외
		
		if(user == null){ System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>index"); //세션없으면 index.html ㄱㄱ 
		response.sendRedirect("index.html"); return false;
		} return super.preHandle(request, response, handler); 
		} 
	}}}}
*/