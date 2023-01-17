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
import com.toppings.server.domain.user.repository.UserRepository;
import com.toppings.server.domain_global.constants.StringReturnMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapService {

	private final ScrapRepository scrapRepository;

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

		if (isDuplicatedScrap(restaurant, user))
			throw new GeneralException(ResponseCode.DUPLICATED_ITEM);

		try {
			scrapRepository.save(getScrap(user, restaurant));
		} catch (Exception e) {
			throw new GeneralException(ResponseCode.DUPLICATED_ITEM);
		}
		restaurant.upScrapCount();

		final AlarmRequest alarmRequest = AlarmRequest.of(user, restaurant, AlarmType.Scrap);
		alarmService.registerAndSendRestaurantAlarm(alarmRequest);

		return StringReturnMessage.REGISTRATION_SUCCESS.getMessage();
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
		System.out.println("user = " + user);
		return Scrap.builder()
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

		final Scrap scrap = scrapRepository.findScrapByRestaurantAndUser(restaurant, getUserById(userId))
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));

		scrapRepository.deleteById(scrap.getId());
		restaurant.downScrapCount();
		return StringReturnMessage.DELETE_SUCCESS.getMessage();
	}

	private Restaurant getRestaurantById(Long id) {
		return restaurantRepository.findRestaurantByIdAndPublicYnNot(id, "N")
			.orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));
	}

	private User getUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(() -> new GeneralException(ResponseCode.NOT_FOUND));
	}
}
