package com.toppings.server.domain.scrap.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.notification.dto.AlarmRequest;
import com.toppings.server.domain.notification.service.AlarmService;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.scrap.entity.Scrap;
import com.toppings.server.domain.scrap.repository.ScrapRepository;
import com.toppings.server.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapService {

	private final ScrapRepository scrapRepository;

	private final RestaurantRepository restaurantRepository;

	private final AlarmService alarmService;

	@Transactional
	public Long register(
		Long restaurantId,
		Long userId
	) {
		final Restaurant restaurant = getRestaurantById(restaurantId);
		final User user = getUser(userId);

		if (isDuplicatedScrap(restaurant, user))
			throw new GeneralException(ResponseCode.DUPLICATED_ITEM);

		final Scrap scrap = scrapRepository.save(getScrap(user, restaurant));
		restaurant.upScrapCount();

		final AlarmRequest alarmRequest = AlarmRequest.of(user, restaurant, AlarmType.Scrap);
		alarmService.registerAndSend(alarmRequest);

		return scrap.getId();
	}

	private boolean isDuplicatedScrap(
		Restaurant restaurant,
		User user
	) {
		return scrapRepository.findScrapByRestaurantAndUser(restaurant, user).isPresent();
	}

	private Scrap getScrap(
		User user,
		Restaurant restaurant
	) {
		return Scrap.builder()
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

		final Scrap scrap = scrapRepository.findScrapByRestaurantAndUser(restaurant, getUser(userId))
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));

		scrapRepository.deleteById(scrap.getId());
		restaurant.downScrapCount();
		return scrap.getId();
	}

	private Restaurant getRestaurantById(Long id) {
		return restaurantRepository.findRestaurantByIdAndPublicYnNot(id, "N")
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));
	}

	private User getUser(Long userId) {
		return User.builder().id(userId).build();
	}
}
