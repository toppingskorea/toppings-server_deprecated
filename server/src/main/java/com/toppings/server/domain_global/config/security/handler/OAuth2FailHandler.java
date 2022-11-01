package com.toppings.server.domain_global.config.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.toppings.common.constants.ResponseCode;

public class OAuth2FailHandler extends SimpleUrlAuthenticationFailureHandler {

	// OAuth2 로그인 과정에서 예외가 발생할 경우 동작할 메소드
	@Override
	public void onAuthenticationFailure(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException exception
	) throws IOException, ServletException {
		System.out.println("------------------- OAuth2FailHandler -------------------");
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ResponseCode.BAD_REQUEST.getMessage());
	}
}
