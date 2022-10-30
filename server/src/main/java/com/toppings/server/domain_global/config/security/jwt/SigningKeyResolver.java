package com.toppings.server.domain_global.config.security.jwt;

import java.security.Key;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;

/*
    # Jwt Token 의 서명값 확인을 도와주는 클래스
 */
public class SigningKeyResolver extends SigningKeyResolverAdapter {

	public static SigningKeyResolver instance = new SigningKeyResolver();

	@Override
	public Key resolveSigningKey(
		JwsHeader jwsHeader,
		Claims claims
	) {
		String kid = jwsHeader.getKeyId();
		if (kid == null)
			return null;
		return JwtKey.getKey(kid);
	}
}
