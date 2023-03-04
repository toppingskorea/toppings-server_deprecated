package com.toppings.server.domain_global.config.security.oauth;

import java.util.Objects;
import java.util.UUID;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.toppings.server.domain.user.constant.Auth;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;
import com.toppings.server.domain_global.config.security.auth.PrincipalDetails;
import com.toppings.server.domain_global.config.security.oauth.provider.KakaoUserInfo;
import com.toppings.server.domain_global.config.security.oauth.provider.OAuth2UserInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		OAuth2UserInfo oAuth2UserInfo = getOAuth2UserInfo(userRequest, oAuth2User);
		try {
			User user = getUser(Objects.requireNonNull(oAuth2UserInfo));
			return new PrincipalDetails(user, oAuth2User.getAttributes());
		} catch (Exception ex) {
			throw new OAuth2AuthenticationException("OAuth2 auth is fail - " + ex.getMessage());
		}
	}

	// OAuth2UserRequest 와 OAuth2User 객체를 활용하여 OAuth2UserInfo 객체 생성하기
	private OAuth2UserInfo getOAuth2UserInfo(
		OAuth2UserRequest userRequest,
		OAuth2User oAuth2User
	) {
		OAuth2UserInfo oAuth2UserInfo = null;
		if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
		}
		return oAuth2UserInfo;
	}

	// OAuth2UserInfo 객체에서 필요한 정보를 통해 사용자 조회 및 없을 경우 등록 시켜주기
	private User getUser(OAuth2UserInfo oAuth2UserInfo) {
		String providerId = oAuth2UserInfo.getProviderId();
		String email = oAuth2UserInfo.getEmail();
		String name = getRandomNickname();
		User user = userRepository.findUserByUsername(providerId).orElse(null);
		if (user == null)
			user = registerUser(providerId, email, name);
		return user;
	}

	private static String getRandomNickname() {
		return "user_" + UUID.randomUUID().toString().split("-")[4];
	}

	// 사용자 등록 (가입)
	private User registerUser(
		String providerId,
		String email,
		String name
	) {
		User user = User.builder()
			.username(providerId)
			.role(Auth.ROLE_TEMP)
			.email(email)
			.name(name)
			.build();
		userRepository.save(user);
		return user;
	}
}
