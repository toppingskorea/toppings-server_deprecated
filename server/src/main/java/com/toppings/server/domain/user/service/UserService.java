package com.toppings.server.domain.user.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.user.dto.UserModifyRequest;
import com.toppings.server.domain.user.dto.UserRegisterRequest;
import com.toppings.server.domain.user.dto.UserResponse;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

	private final UserRepository userRepository;

	/**
	 * Refresh token 수정
	 */
	@Transactional
	public void updateUserRefreshTokenByUserId(
		Long userId,
		String refreshToken
	) {
		userRepository.updateUserRefreshTokenByUserId(userId, refreshToken, "N");
	}

	public Optional<User> findUserByRefreshToken(String refreshToken) {
		return userRepository.findUserByRefreshTokenAndDeleteYn(refreshToken, "N");
	}

	/**
	 * 회원 가입
	 */
	@Transactional
	public UserResponse registerUser(
		UserRegisterRequest request,
		Long id
	) {
		User user = getUserById(id);
		user.setCountry(request.getCountry());
		user.setEatingHabit(List.of(request.getHabit()));
		return UserResponse.entityToDto(user);
	}

	@Transactional
	public UserResponse modifyUser(
		UserModifyRequest userModifyRequest,
		Long id
	) {
		User user = getUserById(id);
		user.setName(userModifyRequest.getName() != null ? userModifyRequest.getName() : user.getName());
		user.setCountry(userModifyRequest.getCountry() != null ? userModifyRequest.getCountry() : user.getCountry());
		user.setEatingHabit(userModifyRequest.getHabit() != null ?
				List.of(userModifyRequest.getHabit()) : user.getEatingHabit());
		return UserResponse.entityToDto(user);
	}

	private User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}
}
