package com.toppings.server.domain_global.config.security.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toppings.common.constants.ResponseCode;
import com.toppings.common.dto.ApiDataResponse;
import com.toppings.server.domain.user.constant.Auth;
import com.toppings.server.domain.user.dto.UserLoginRequest;
import com.toppings.server.domain.user.service.UserService;
import com.toppings.server.domain_global.config.security.auth.PrincipalDetails;
import com.toppings.server.domain_global.config.security.jwt.JwtProperties;
import com.toppings.server.domain_global.config.security.jwt.JwtUtils;

/*
    # 인증을 위한 필터
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	private final ObjectMapper objectMapper = new ObjectMapper();

	private final UserService userService;

	public JwtAuthenticationFilter(
		AuthenticationManager authenticationManager,
		UserService userService
	) {
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}

	@Override
	public Authentication attemptAuthentication(
		HttpServletRequest request,
		HttpServletResponse response
	) throws AuthenticationException {
		return authenticationManager.authenticate(getUsernamePasswordAuthenticationToken(getAdminLoginDto(request)));
	}

	@Override
	protected void successfulAuthentication(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain chain,
		Authentication authResult
	) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		Auth role = principalDetails.getUser().getRole();
		if (role.equals(Auth.ROLE_ADMIN))
			JwtUtils.makeAccessTokenCookie(response, principalDetails.getUser());
		else
			response.addHeader(JwtProperties.JWT_ACCESS_HEADER, JwtUtils.createAccessToken(principalDetails.getUser()));

		PrintWriter writer = response.getWriter();
		ApiDataResponse<Auth> apiDataResponse = ApiDataResponse.of(role);
		writer.println(objectMapper.writeValueAsString(apiDataResponse));
		writer.flush();
		writer.close();
	}

	@Override
	protected void unsuccessfulAuthentication(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException failed
	) throws IOException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
	}

	private UserLoginRequest getAdminLoginDto(HttpServletRequest request) {
		try {
			return objectMapper.readValue(request.getInputStream(), UserLoginRequest.class);
		} catch (IOException exception) {
			throw new AuthenticationCredentialsNotFoundException(ResponseCode.BAD_REQUEST.getMessage());
		}
	}

	private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(
		UserLoginRequest userLoginRequest
	) {
		return new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword(),
			new ArrayList<>()
		);
	}
}
