package com.toppings.server.domain_global.config.security.jwt.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import com.toppings.common.constants.prop.CorsProperties;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.service.UserService;
import com.toppings.server.domain_global.config.aop.RequestLogAspect;
import com.toppings.server.domain_global.config.security.jwt.JwtProperties;
import com.toppings.server.domain_global.config.security.jwt.JwtUtils;

/*
    # 인가를 위한 필터
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final UserService userService;

	private final ServletContext servletContext;

	private static final Logger logger = LoggerFactory.getLogger(RequestLogAspect.class);

	private final List<String> nonAuthUrl = List.of("none");

	private final List<String> nonAuthPathVariableUrl = List.of("none");

	private final boolean isProd;

	public JwtAuthorizationFilter(
		AuthenticationManager authenticationManager,
		UserService userService,
		ServletContext servletContext,
		boolean isProd
	) {
		super(authenticationManager);
		this.userService = userService;
		this.servletContext = servletContext;
		this.isProd = isProd;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws IOException, ServletException {
		String requestUrl = request.getRequestURI();
		String method = request.getMethod();
		if (isDomainCheck(request) && isNonAuthUrl(requestUrl, method)) {
			String accessToken = getAccessToken(request);
			String refreshToken = getRefreshToken(request);
			if (idValidAccessToken(accessToken)) {
				logger.debug("accessToken : " + accessToken);
				addAuthenticationTokenInSecurityContext(accessToken);
			} else if (isValidRefreshToken(refreshToken)) {
				logger.debug("refreshToken : " + refreshToken);
				addAuthenticationAfterRefreshTokenValidation(response, refreshToken);
			}
		}
		chain.doFilter(request, response);
	}

	private boolean isDomainCheck(HttpServletRequest request) {
		String referer = request.getHeader("referer");
		String domain = "";
		if (referer != null) {
			domain = referer.split("/")[2];
			logger.debug("domain : " + domain);
		}

		if (isProd)
			return CorsProperties.prodDomains.contains(domain);
		else
			return true;
	}

	private boolean isPathVariable(String url) {
		String subUrl = url.substring(url.lastIndexOf("/") + 1);
		return subUrl.chars().allMatch(Character::isDigit);
	}

	private String getUrlWithoutPathVariable(String url) {
		return url.substring(url.indexOf("/"), url.lastIndexOf("/"));
	}

	private boolean isNonAuthPathVariableUrl(
		String url,
		String method
	) {
		return isPathVariable(url) && method.equals("GET") && nonAuthPathVariableUrl.contains(
			getUrlWithoutPathVariable(url));
	}

	private boolean isNonAuthUrl(
		String requestUrl,
		String method
	) {
		if (nonAuthUrl.contains(requestUrl))
			return false;
		else
			return !isNonAuthPathVariableUrl(requestUrl, method);
	}

	private void addAuthenticationAfterRefreshTokenValidation(
		HttpServletResponse response,
		String refreshToken
	) {
		User user = userService.findUserByRefreshToken(refreshToken).orElse(null);
		if (user != null) {
			String newAccessToken = createNewAccessToken(response, user);
			logger.debug("newAccessToken : " + newAccessToken);
			addAuthenticationTokenInSecurityContext(newAccessToken);
			// String newRefreshToken = JwtUtils.makeRefreshTokenCookie(response, user.getUserId());
			// userRepository.updateUserRefreshTokenByUserId(user.getUserId(), newRefreshToken);
			/*
				- 리프레시 토큰 탈취
					- 악의적인 사용자가 리프레시 토큰 탈취 시 access token 을 만료시간까지 계속 발급받을 수 있다.
				- 대응방안
					1. refresh token 을 access token 과 같이 갱신 후 refresh token 이 다르면 폐기
					2. access token 도 같이 1대1로 저장 후 다를 경우 폐기 (redis)
			*/
		} /*else {
			logger.info("remove refresh token cookie");
			Long userId = JwtUtils.getUserId(refreshToken);
			userRepository.updateUserRefreshTokenByUserId(userId, null);
			removeRefreshTokenCookie(response);
		}*/
	}

	private void removeRefreshTokenCookie(HttpServletResponse response) {
		Cookie refreshTokenCookie = new Cookie(JwtProperties.JWT_REFRESH_HEADER, null);
		refreshTokenCookie.setMaxAge(0);
		response.addCookie(refreshTokenCookie);
	}

	private String createNewAccessToken(
		HttpServletResponse response,
		User user
	) {
		String newAccessToken = JwtUtils.createAccessToken(user);
		response.addHeader(JwtProperties.JWT_ACCESS_HEADER, newAccessToken);
		return newAccessToken;
	}

	private String createNewAccessTokenWithOutHeader(User user) {
		return JwtUtils.createAccessToken(user);
	}

	private void addAuthenticationTokenInSecurityContext(String accessToken) {
		Long userId = JwtUtils.getUserId(accessToken);
		String role = JwtUtils.getUserRole(accessToken);
		logger.debug("role : " + role);
		logger.debug("userId : " + userId);
		SecurityContextHolder.getContext().setAuthentication(getAuthenticationToken(userId, role));
	}

	private UsernamePasswordAuthenticationToken getAuthenticationToken(
		Long userId,
		String role
	) {
		if (userId != null) {
			logger.debug("add auth token");
			return new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority(role)));
		}
		return null;
	}

	private String getRefreshToken(HttpServletRequest request) {
		try {
			return Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals(JwtProperties.JWT_REFRESH_HEADER))
				.findFirst()
				.map(Cookie::getValue)
				.orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	private String getAccessToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(JwtProperties.JWT_ACCESS_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtProperties.TOKEN_PREFIX))
			return bearerToken.substring(7);
		else
			return null;
	}

	private boolean isValidRefreshToken(String refreshToken) {
		return refreshToken != null && JwtUtils.validateToken(refreshToken);
	}

	private boolean idValidAccessToken(String accessToken) {
		return accessToken != null && JwtUtils.validateToken(accessToken);
	}
}
