package com.toppings.server.domain_global.config.security.jwt.filter;

import static org.springframework.util.StringUtils.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.FilterChain;
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

import com.toppings.common.constants.prop.CorsProperties;
import com.toppings.server.domain.user.service.UserService;
import com.toppings.server.domain_global.config.aop.RequestLogAspect;
import com.toppings.server.domain_global.config.security.jwt.JwtProperties;
import com.toppings.server.domain_global.config.security.jwt.JwtUtils;

/*
    # 인가를 위한 필터
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(RequestLogAspect.class);

	private final boolean isProd;

	public JwtAuthorizationFilter(
		AuthenticationManager authenticationManager,
		UserService userService,
		boolean isProd
	) {
		super(authenticationManager);
		this.userService = userService;
		this.isProd = isProd;
	}

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain
	) throws IOException, ServletException {
		if (isDomainCheck(request)) {
			String accessToken = getAccessToken(request);
			String accessCookieToken = getAccessTokenByCookie(request);
			if (idValidAccessToken(accessToken))
				addAuthenticationTokenInSecurityContext(accessToken);

			else if (idValidAccessToken(accessCookieToken))
				addAuthenticationTokenInSecurityContext(accessCookieToken);
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

	private void addAuthenticationTokenInSecurityContext(String accessToken) {
		Long userId = JwtUtils.getUserId(accessToken);
		logger.debug("userId : " + userId);
		SecurityContextHolder.getContext()
			.setAuthentication(getAuthenticationToken(userId, userService.findUserRole(userId)));
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

	private String getAccessToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(JwtProperties.JWT_ACCESS_HEADER);
		if (hasText(bearerToken) && bearerToken.startsWith(JwtProperties.TOKEN_PREFIX))
			return bearerToken.substring(7);
		else
			return null;
	}

	private String getAccessTokenByCookie(HttpServletRequest request) {
		try {
			return Arrays.stream(request.getCookies())
				.filter(cookie -> cookie.getName().equals(JwtProperties.JWT_ACCESS_COOKIE))
				.findFirst()
				.map(Cookie::getValue)
				.orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	private boolean idValidAccessToken(String accessToken) {
		return accessToken != null && JwtUtils.validateToken(accessToken);
	}
}
