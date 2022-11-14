package com.toppings.server.domain.likes.service;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.likes.entity.Likes;
import com.toppings.server.domain.likes.repository.LikeRepository;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;

    private final RestaurantRepository restaurantRepository;

    @Transactional
	public Long register(
		Long restaurantId,
		Long userId
	) {
		Restaurant restaurant = getRestaurantById(restaurantId);

		Likes like = likeRepository.save(getLikes(userId, restaurant));
		return like.getId();
	}

	private Restaurant getRestaurantById(Long restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	private Likes getLikes(
		Long userId,
		Restaurant restaurant
	) {
		return Likes.builder()
			.restaurant(restaurant)
			.user(getUser(userId))
			.build();
	}

	@Transactional
	public Long remove(
		Long restaurantId,
		Long userId
	) {
		Restaurant restaurant = getRestaurantById(restaurantId);

		Likes like = likeRepository.findLikesByRestaurantAndUser(restaurant, getUser(userId))
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));

		likeRepository.deleteById(like.getId());
		return like.getId();
	}

	private User getUser(Long userId) {
		return User.builder().id(userId).build();
	}
}
