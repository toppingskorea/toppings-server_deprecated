package com.toppings.server.domain_global.config.security.jwt;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.security.Keys;

/*
    # Jwt Key 를 제공하는 클래스
 */
@Component
public class JwtKey {

	private static final Map<String, String> SECRET_KEY_SET = new HashMap<>();
	private static final List<String> KID_SET = new ArrayList<>();
	private static final Random randomIndex = new Random();

	private static final String FIRST_KEY = "firstKey";
	private static final String SECOND_KEY = "secondKey";
	private static final String THIRD_KEY = "thirdKey";

	public static Pair<String, Key> getRandomKey() {
		String kid = KID_SET.get(randomIndex.nextInt(KID_SET.size()));
		String secretKey = SECRET_KEY_SET.get(kid);
		return Pair.of(kid, Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)));
	}

	public static Key getKey(String kid) {
		String key = SECRET_KEY_SET.getOrDefault(kid, null);
		if (key == null)
			return null;
		return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
	}

	@Value("${jwt.firstKey}")
	public void setFirstKey(String firstKey) {
		SECRET_KEY_SET.put(FIRST_KEY, firstKey);
		KID_SET.add(FIRST_KEY);
	}

	@Value("${jwt.secondKey}")
	public void setSecondKey(String secondKey) {
		SECRET_KEY_SET.put(SECOND_KEY, secondKey);
		KID_SET.add(SECOND_KEY);
	}

	@Value("${jwt.thirdKey}")
	public void setThirdKey(String thirdKey) {
		SECRET_KEY_SET.put(THIRD_KEY, thirdKey);
		KID_SET.add(THIRD_KEY);
	}
}
