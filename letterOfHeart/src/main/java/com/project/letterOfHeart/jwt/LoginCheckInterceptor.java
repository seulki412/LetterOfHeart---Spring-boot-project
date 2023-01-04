package com.project.letterOfHeart.jwt;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Log4j
public class LoginCheckInterceptor implements HandlerInterceptor {

	private JwtTokenProvider jwtTokenProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String requestURI = request.getRequestURI();
		System.out.println("[interceptor] : " + requestURI);
		// Thread.sleep(50000);
		Cookie[] list = request.getCookies();

		String token = "";

		if (list != null) {

			boolean Flag = false;

			for (Cookie cookie : list) {
				if (cookie.getName().equals("accessToken")) {
					token = cookie.getValue();
					Flag = true;

					System.out.println("[인증된 사용자 요청]");
					System.out.println("뭐로오나요? : " + requestURI);
					log.info("뭐로오나요? : " + requestURI);

					//Thread.sleep(5000);
				}
			}

			if (Flag) {
				if( "/".equals( requestURI ) ) {
					System.out.println("여기로오는게 맞지") ;
					response.sendRedirect( "/users/logout" );
				} else {
					response.sendRedirect( requestURI );
				}
				
				return true ;
			} else {
				System.out.println("여기올텐데?");
				response.sendRedirect( "/users/logout" );
				return true ;
			}
			
		} else {
			System.out.println("[미인증 사용자 요청]");
			if (requestURI.equals("/")) {
				return true;
			}
		}
		// 로그인 되어있을 떄
		return true;
	}

}
