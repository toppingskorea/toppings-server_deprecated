package com.toppings.server.domain_global.config.security.jwt;

import java.security.Key;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.util.Pair;

import com.toppings.server.domain.user.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;

/*
    # Jwt Token 을 실제로 다루는 클래스
 */
public class JwtUtils {

	private static final String ROLE_WORD = "role";

	public static String getUserRole(String token) {
		return Jwts.parserBuilder()
			.setSigningKeyResolver(SigningKeyResolver.instance)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get(ROLE_WORD, String.class);
	}

	public static Long getUserId(String token) {
		return Jwts.parserBuilder()
			.setSigningKeyResolver(SigningKeyResolver.instance)
			.build()
			.parseClaimsJws(token)
			.getBody()
			.get("uid", Long.class);
	}

	public static String createAccessToken(User user) {
		Date now = new Date();
		Pair<String, Key> key = JwtKey.getRandomKey();
		return Jwts.builder()
			.setClaims(getClaims(user))
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + JwtProperties.ACCESS_EXPIRATION_TIME))
			.setHeaderParam(JwsHeader.KEY_ID, key.getFirst())
			.signWith(key.getSecond())
			.compact();
	}

	public static String createRefreshToken(Long id) {
		Date now = new Date();
		Pair<String, Key> key = JwtKey.getRandomKey();
		return Jwts.builder()
			.setClaims(getClaims(id))
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + JwtProperties.REFRESH_EXPIRATION_TIME))
			.setHeaderParam(JwsHeader.KEY_ID, key.getFirst())
			.signWith(key.getSecond())
			.compact();
	}

	public static String makeRefreshTokenCookie(
		HttpServletResponse response,
		Long id
	) {
		String refreshToken = JwtUtils.createRefreshToken(id);
		Cookie cookie = new Cookie(JwtProperties.JWT_REFRESH_HEADER, refreshToken);
		cookie.setMaxAge(JwtProperties.REFRESH_COOKIE_EXPIRATION_TIME);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
		return refreshToken;
	}

	private static Claims getClaims(User user) {
		Claims claims = Jwts.claims();
		claims.put(ROLE_WORD, user.getRole());
		claims.put("uid", user.getUserId());
		return claims;
	}

	private static Claims getClaims(Long id) {
		Claims claims = Jwts.claims();
		claims.put("uid", id);
		return claims;
	}

	public static boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKeyResolver(SigningKeyResolver.instance).build().parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
