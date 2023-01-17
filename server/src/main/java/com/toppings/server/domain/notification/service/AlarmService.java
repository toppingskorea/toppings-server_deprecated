package com.toppings.server.domain.notification.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.toppings.server.domain.review.entity.Review;
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
	public Page<AlarmResponse> findAlarms(
		Long userId,
		Pageable pageable
	) {
		final User user = getUserById(userId);
		return alarmRepository.findAlarms(user.getId(), pageable);
	}

	private User getUserById(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	/*
		음식점 알람 저장 및 전송
	 */
	@Transactional
	public void registerRestaurantAlarm(
		AlarmRequest alarmRequest,
		User fromUser,
		User toUser
	) {
		// TODO: Alarm 중복 등록 방지
		final Restaurant restaurant = alarmRequest.getRestaurant();
		final Alarm alarm = Alarm.of(fromUser, restaurant, alarmRequest.getContent(), alarmRequest.getType());
		final Alarm savedAlarm = alarmRepository.save(alarm);

		final AlarmResponse alarmResponse = AlarmResponse.of(restaurant, fromUser, savedAlarm);
		sendAlarm(toUser, alarmResponse);
	}

	@Transactional
	public void registerReviewAlarm(
		AlarmRequest alarmRequest,
		User fromUser,
		User toUser
	) {
		// TODO: Alarm 중복 등록 방지
		final Review review = alarmRequest.getReview();
		final Alarm alarm = Alarm.of(fromUser, review, alarmRequest.getContent(), alarmRequest.getType());
		final Alarm savedAlarm = alarmRepository.save(alarm);

		final AlarmResponse alarmResponse = AlarmResponse.of(review, fromUser, savedAlarm);
		sendAlarm(toUser, alarmResponse);
	}

	private void sendAlarm(
		User toUser,
		AlarmResponse alarmResponse
	) {
		template.convertAndSend("/sub/" + toUser.getId(), alarmResponse);
	}

	@Transactional
	public void removeAlarm(Restaurant restaurant) {
		alarmRepository.deleteBatchByRestaurant(restaurant);
	}
}
