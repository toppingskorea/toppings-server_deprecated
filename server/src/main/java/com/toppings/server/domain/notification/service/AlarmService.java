package com.toppings.server.domain.notification.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.notification.dto.AlarmRequest;
import com.toppings.server.domain.notification.dto.AlarmResponse;
import com.toppings.server.domain.notification.entity.Alarm;
import com.toppings.server.domain.notification.repository.AlarmRepository;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.review.entity.Review;
import com.toppings.server.domain.review.repository.ReviewRepository;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

	private final AlarmRepository alarmRepository;

	private final UserRepository userRepository;

	private final ReviewRepository reviewRepository;

	private final RestaurantRepository restaurantRepository;

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
		return userRepository.findById(id).orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));
	}

	private Review getReviewByIdAndPubYn(Long id) {
		return reviewRepository.findReviewByIdAndPublicYnNot(id, "N")
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));
	}

	private Review getReviewById(Long id) {
		return reviewRepository.findById(id)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));
	}

	private Restaurant getRestaurantByIdAndPubYn(Long id) {
		return restaurantRepository.findRestaurantByIdAndPublicYnNot(id, "N")
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));
	}

	private Restaurant getRestaurantById(Long id) {
		return restaurantRepository.findById(id)
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));
	}

	/*
		음식점 알람 저장 및 전송
	 */
	@Transactional
	public Long registerAlarm(
		Long userId,
		AlarmRequest alarmRequest
	) {
		final User fromUser = getUserById(userId);
		final AlarmType alarmType = alarmRequest.getType();

		final Alarm alarm;
		final User toUser;

		if (alarmType.equals(AlarmType.RejectReview)) {
			final Review review = alarmRequest.isRejectType() ?
				getReviewById(alarmRequest.getId()) : getReviewByIdAndPubYn(alarmRequest.getId());
			toUser = review.getUser();
			alarm = getReviewAlarm(fromUser, alarmType, toUser, review);
		} else {
			final Restaurant restaurant = alarmRequest.isRejectType() ?
				getRestaurantById(alarmRequest.getId()) : getRestaurantByIdAndPubYn(alarmRequest.getId());
			toUser = restaurant.getUser();
			alarm = getRestaurantAlarm(fromUser, alarmType, toUser, restaurant, alarmRequest);
		}

		try {
			alarmRepository.save(alarm);
		} catch (Exception e) {
			throw new GeneralException(ResponseCode.DUPLICATED_USER);
		}

		AlarmResponse alarmResponse = AlarmResponse.of(fromUser, alarm);
		sendAlarm(toUser, alarmResponse);
		return alarm.getId();
	}

	private Alarm getRestaurantAlarm(
		User user,
		AlarmType alarmType,
		User toUser,
		Restaurant restaurant,
		AlarmRequest alarmRequest
	) {
		String content = alarmType.equals(AlarmType.RejectRestaurant)
			? restaurant.getCause() : alarmRequest.getContent();
		final Alarm alarm = Alarm.of(user, restaurant, content, alarmType);
		alarm.updateCode(generateId(alarmType, user, toUser, String.valueOf(restaurant.getCode())));
		return alarm;
	}

	private Alarm getReviewAlarm(
		User user,
		AlarmType alarmType,
		User toUser,
		Review review
	) {
		final Alarm alarm = Alarm.of(user, review, review.getCause(), alarmType);
		alarm.updateCode(generateId(alarmType, user, toUser, String.valueOf(review.getId())));
		return alarm;
	}

	private String generateId(
		AlarmType type,
		User fromUser,
		User toUser,
		String id
	) {
		StringBuilder builder = new StringBuilder(type.name());
		builder.append("_")
			.append(isRejectAlarm(type) ? toUser.getUsername() : fromUser.getUsername())
			.append("_")
			.append(id)
			.append(UUID.randomUUID());
		return builder.toString();
	}

	private boolean isRejectAlarm(AlarmType type) {
		return type.equals(AlarmType.RejectReview) || type.equals(AlarmType.RejectRestaurant);
	}

	private void sendAlarm(
		User toUser,
		AlarmResponse alarmResponse
	) {
		template.convertAndSend("/sub/" + toUser.getId(), alarmResponse);
	}
}
