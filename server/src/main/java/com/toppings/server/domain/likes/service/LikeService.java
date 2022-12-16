package com.toppings.server.domain.likes.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.likes.entity.Likes;
import com.toppings.server.domain.likes.repository.LikeRepository;
import com.toppings.server.domain.notification.constant.AlarmMessage;
import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.notification.dto.AlarmResponse;
import com.toppings.server.domain.notification.entity.Alarm;
import com.toppings.server.domain.notification.repository.AlarmRepository;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain_global.utils.notification.AlarmSender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

	private final LikeRepository likeRepository;

	private final RestaurantRepository restaurantRepository;

	private final AlarmRepository alarmRepository;

	private final AlarmSender alarmSender;

	@Transactional
	public Long register(
		Long restaurantId,
		Long userId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		final User user = getUser(userId);

		if (isDuplicatedLikes(restaurant, user))
			throw new GeneralException(ResponseCode.DUPLICATED_ITEM);

		final Likes like = likeRepository.save(getLikes(user, restaurant));
		restaurant.setLikeCount(restaurant.getLikeCount() + 1);

		final User alarmUser = restaurant.getUser();
		saveAndSendAlarm(restaurant, alarmUser);

		return like.getId();
	}

	private void saveAndSendAlarm(
		Restaurant restaurant,
		User alarmUser
	) {
		final Alarm alarm = Alarm.of(alarmUser, restaurant, null, AlarmType.Like);
		final Alarm savedAlarm = alarmRepository.save(alarm);
		alarmSender.send(restaurant, alarmUser, savedAlarm, AlarmMessage.LikeMessage.getMessage());
	}

	private boolean isDuplicatedLikes(
		Restaurant restaurant,
		User user
	) {
		return likeRepository.findLikesByRestaurantAndUser(restaurant, user).isPresent();
	}

	// TODO : public yn
	private Restaurant getRestaurantById(Long restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	private Likes getLikes(
		User user,
		Restaurant restaurant
	) {
		return Likes.builder()
			.restaurant(restaurant)
			.user(user)
			.build();
	}

	@Transactional
	public Long remove(
		Long restaurantId,
		Long userId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);

		final Likes like = likeRepository.findLikesByRestaurantAndUser(restaurant, getUser(userId))
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));

		likeRepository.deleteById(like.getId());
		restaurant.setLikeCount(restaurant.getLikeCount() == 0 ? 0 : restaurant.getLikeCount() - 1);
		return like.getId();
	}

	private User getUser(Long userId) {
		return User.builder().id(userId).build();
	}
}
