package com.toppings.server.domain.likes.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.likes.entity.Likes;
import com.toppings.server.domain.likes.repository.LikeRepository;
import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.notification.dto.AlarmRequest;
import com.toppings.server.domain.notification.service.AlarmService;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;
import com.toppings.server.domain_global.constants.StringReturnMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

	private final LikeRepository likeRepository;

	private final RestaurantRepository restaurantRepository;

	private final UserRepository userRepository;

	private final AlarmService alarmService;

	@Transactional
	public String register(
		Long restaurantId,
		Long userId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		final User user = getUserById(userId);

		if (isDuplicatedLikes(restaurant, user))
			throw new GeneralException(ResponseCode.DUPLICATED_ITEM);

		try {
			likeRepository.save(getLikes(user, restaurant));
		} catch (Exception e) {
			throw new GeneralException(ResponseCode.DUPLICATED_ITEM);
		}
		restaurant.upLikeCount();

		final AlarmRequest alarmRequest = AlarmRequest.of(user, restaurant, AlarmType.Like);
		alarmService.registerAndSendRestaurantAlarm(alarmRequest);

		return StringReturnMessage.REGISTRATION_SUCCESS.getMessage();
	}

	private boolean isDuplicatedLikes(
		Restaurant restaurant,
		User user
	) {
		return likeRepository.findLikesByRestaurantAndUser(restaurant, user).isPresent();
	}

	private Restaurant getRestaurantById(Long id) {
		return restaurantRepository.findRestaurantByIdAndPublicYnNot(id, "N")
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));
	}

	private Likes getLikes(
		User user,
		Restaurant restaurant
	) {
		return Likes.builder()
			.id(user.getUsername() + "_" + restaurant.getCode())
			.restaurant(restaurant)
			.user(user)
			.build();
	}

	@Transactional
	public String remove(
		Long restaurantId,
		Long userId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);

		final Likes like = likeRepository.findLikesByRestaurantAndUser(restaurant, getUserById(userId))
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));

		likeRepository.deleteById(like.getId());
		restaurant.downLikeCount();
		return StringReturnMessage.DELETE_SUCCESS.getMessage();
	}

	private User getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));
	}
}
