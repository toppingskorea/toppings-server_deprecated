package com.toppings.server.domain_global.config.security.oauth.provider;

import com.toppings.server.domain_global.constants.LoginType;

// OAuth2User 객체에서 필요한 리소스 정보를 받을 포맷
public interface OAuth2UserInfo {

	String getProviderId();

	String getProvider();

	String getEmail();

	String getName();

	LoginType getLoginType();

	// String getPhone();
}
