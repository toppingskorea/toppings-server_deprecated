package com.toppings.server.domain_global.config.security.oauth.provider;

import java.util.Map;

import com.toppings.server.domain_global.constants.LoginType;

public class KakaoUserInfo implements OAuth2UserInfo {

	private Map<String, Object> attributes;

	public KakaoUserInfo(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		return String.valueOf(attributes.get("id"));
	}

	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getEmail() {
		Map<String, String> map = (Map)attributes.get("kakao_account");
		return (String)map.get("email");
	}

	@Override
	public String getName() {
		System.out.println("==================================================================");
		Map<String, String> map = (Map)attributes.get("properties");
		return (String)map.get("nickname");
	}

	@Override
	public LoginType getLoginType() {
		return LoginType.KAKAO;
	}

    /*@Override
    public String getPhone() {
    	return null;
    }*/
}
