package com.toppings.server.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.user.dto.AdminRegisterRequest;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	/**
	 * 관리자 등록
	 */
	@Transactional
	public Long register(AdminRegisterRequest registerRequest) {
		if (isDuplicated(registerRequest))
			throw new GeneralException(ResponseCode.DUPLICATED_USER);

		final User user = AdminRegisterRequest.dtoToEntity(registerRequest);
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		userRepository.save(user);
		return user.getId();
	}

	private boolean isDuplicated(AdminRegisterRequest registerRequest) {
		return userRepository.findUserByUsername(registerRequest.getUsername()).isPresent();
	}
}
