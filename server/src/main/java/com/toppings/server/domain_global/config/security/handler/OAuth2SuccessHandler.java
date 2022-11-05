package com.toppings.server.domain_global.config.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import com.toppings.server.domain_global.config.security.auth.PrincipalDetails;
import com.toppings.server.domain_global.config.security.jwt.JwtUtils;

public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	// OAuth2 로그인 과정을 성공적으로 거칠 경우 동작하는 메소드
	// 요구사항에 따라 언제든 수정될 수 있다.
	@Override
	public void onAuthenticationSuccess(
		HttpServletRequest request,
		HttpServletResponse response,
		Authentication authentication
	) throws IOException, ServletException {
		System.out.println("--------------- oauth2 success handler ---------------");
		PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
		String accessToken = JwtUtils.createAccessToken(principalDetails.getUser());
		String refreshToken = JwtUtils.createRefreshToken(principalDetails.getUser().getId());
		String targetUrl = getTargetUrl(accessToken, refreshToken);
		getRedirectStrategy().sendRedirect(request, response, targetUrl); // 나중에 도메인 주소로 변경
	}

	private String getTargetUrl(
		String accessToken,
		String refreshToken
	) {
		return UriComponentsBuilder.fromUriString("http://127.0.0.1:3000/login/redirect")
			.queryParam("accessToken", accessToken)
			.queryParam("refreshToken", refreshToken)
			.build().toUriString();
	}
}

/*
	여러 상황이 존재한다.
		- 회원가입 x
			1. oauth2 과정을 거친 후 가져온 정보들로 간편가입 (이때 쿠키와 헤더 반환)
			2. 이 후 추가정보 입력페이지로 관련 정보가 필요한 요청 시 유도
		- 회원가입 o
			1. oauth2 과정을 거친 후 회원가입 url로 리다이렉트
			2. 리다이렉트 시 header 같은 곳에 가져온 사용자 고유 id와 email 정보를 담는다.
			3. 해당 정보와 추가 입력 정보를 통해 사용자는 회원가입 진행
			4. 이 후 db에 사용자 있을 시 리다이렉트 주소를 회원가입 페이지에서 다른 곳으로 변경 후 로그인 처리 (이때 쿠키와 헤더 반환)
 */
