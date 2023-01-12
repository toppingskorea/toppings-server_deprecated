package com.toppings.server.domain.notification.service;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.notification.dto.AlarmRequest;
import com.toppings.server.domain.notification.dto.AlarmResponse;
import com.toppings.server.domain.notification.entity.Alarm;
import com.toppings.server.domain.notification.repository.AlarmRepository;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

	private final AlarmRepository alarmRepository;

	private final UserRepository userRepository;

	private final SimpMessagingTemplate template;

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

	/*
		알람 저장 및 전송
	 */
	@Transactional
	public void registerAndSend(AlarmRequest alarmRequest) {
		final Restaurant restaurant = alarmRequest.getRestaurant();
		final User toUser = restaurant.getUser();
		final User fromUser = alarmRequest.getFromUser();

		final Alarm alarm = Alarm.of(fromUser, restaurant, alarmRequest.getContent(), alarmRequest.getType());
		final Alarm savedAlarm = alarmRepository.save(alarm);

		final AlarmResponse alarmResponse;

		// TODO: Refactoring Pick
		if (fromUser != null)
			alarmResponse = AlarmResponse.of(restaurant, fromUser, savedAlarm);
		else
			alarmResponse = AlarmResponse.of(restaurant, savedAlarm);
		template.convertAndSend("/sub/" + toUser.getId(), alarmResponse);
	}

	@Transactional
	public void removeAlarm(Restaurant restaurant) {
		alarmRepository.deleteBatchByRestaurant(restaurant);
	}
}
