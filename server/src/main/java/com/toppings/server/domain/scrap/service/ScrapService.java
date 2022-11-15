package com.toppings.server.domain.scrap.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.scrap.entity.Scrap;
import com.toppings.server.domain.scrap.repository.ScrapRepository;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapService {

	private final ScrapRepository scrapRepository;

	private final RestaurantRepository restaurantRepository;

	@Transactional
	public Long register(
		Long restaurantId,
		Long userId
	) {
		Restaurant restaurant = getRestaurantById(restaurantId);

		Scrap scrap = scrapRepository.save(getScrap(userId, restaurant));
		restaurant.setScrapCount(restaurant.getScrapCount() + 1);
		return scrap.getId();
	}

	private Scrap getScrap(
		Long userId,
		Restaurant restaurant
	) {
		return Scrap.builder()
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

		Scrap scrap = scrapRepository.findScrapByRestaurantAndUser(restaurant, getUser(userId))
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));

		scrapRepository.deleteById(scrap.getId());
		restaurant.setScrapCount(restaurant.getScrapCount() == 0 ? 0 : restaurant.getScrapCount() - 1);
		return scrap.getId();
	}

	private Restaurant getRestaurantById(Long restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	private User getUser(Long userId) {
		return User.builder().id(userId).build();
	}
}
