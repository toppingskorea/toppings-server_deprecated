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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

	private final LikeRepository likeRepository;

	private final RestaurantRepository restaurantRepository;

	private final AlarmService alarmService;

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
		restaurant.upLikeCount();

		final AlarmRequest alarmRequest = AlarmRequest.of(user, restaurant, AlarmType.Like);
		alarmService.registerAndSend(alarmRequest);

		return like.getId();
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
		restaurant.downLikeCount();
		return like.getId();
	}

	private User getUser(Long userId) {
		return User.builder().id(userId).build();
	}
}
