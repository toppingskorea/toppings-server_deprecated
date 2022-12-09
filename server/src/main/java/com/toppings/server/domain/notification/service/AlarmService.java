package com.toppings.server.domain.notification.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.notification.dto.AlarmResponse;
import com.toppings.server.domain.notification.repository.AlarmRepository;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmService {

	private final AlarmRepository alarmRepository;

	private final UserRepository userRepository;

	/**
	 * 최근 알람 조회 (가입 이후 모든 알림이 날짜 최신순으로 정렬 되어서 보여지는 것)
	 */
	public List<AlarmResponse> findAlarms(Long userId) {
		final User user = getUserById(userId);
		return alarmRepository.findAlarms(user.getId());
	}

	private User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}
}
