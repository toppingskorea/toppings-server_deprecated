package com.toppings.server.domain_global.config.security.jwt;

/*
    # Jwt 설정 정보를 모아둔 클래스
 */
public class JwtProperties {
	public static final int ACCESS_EXPIRATION_TIME = 600000 * 6 * 24 * 2; // 48 hour
	public static final int REFRESH_EXPIRATION_TIME = 600000 * 6 * 6; // 6 hour
	public static final int REFRESH_COOKIE_EXPIRATION_TIME = 60 * 60 * 24; // 24 hour
	public static final int ACCESS_COOKIE_EXPIRATION_TIME = 60 * 60 * 24; // 24 hour
	public static final String JWT_ACCESS_HEADER = "Authorization";
	public static final String JWT_ACCESS_COOKIE = "sgnippot-ac";
	public static final String JWT_REFRESH_HEADER = "sgnippot";
	public static final String TOKEN_PREFIX = "Bearer ";
}
