package com.toppings.server.domain.scrap.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.exception.GeneralException;
import com.toppings.server.domain.notification.constant.AlarmMessage;
import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.notification.dto.AlarmResponse;
import com.toppings.server.domain.notification.entity.Alarm;
import com.toppings.server.domain.notification.repository.AlarmRepository;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.repository.RestaurantRepository;
import com.toppings.server.domain.scrap.entity.Scrap;
import com.toppings.server.domain.scrap.repository.ScrapRepository;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain_global.utils.notification.AlarmSender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapService {

	private final ScrapRepository scrapRepository;

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

		if (isDuplicatedScrap(restaurant, user))
			throw new GeneralException(ResponseCode.DUPLICATED_ITEM);

		final Scrap scrap = scrapRepository.save(getScrap(user, restaurant));
		restaurant.setScrapCount(restaurant.getScrapCount() + 1);

		final User alarmUser = restaurant.getUser();
		saveAndSendAlarm(restaurant, alarmUser);

		return scrap.getId();
	}

	private void saveAndSendAlarm(
		Restaurant restaurant,
		User alarmUser
	) {
		final Alarm alarm = Alarm.of(alarmUser, restaurant, null, AlarmType.Scrap);
		final Alarm savedAlarm = alarmRepository.save(alarm);
		alarmSender.send(restaurant, alarmUser, savedAlarm, AlarmMessage.ScrapMessage.getMessage());
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
		restaurant.setScrapCount(restaurant.getScrapCount() == 0 ? 0 : restaurant.getScrapCount() - 1);
		return scrap.getId();
	}

	// TODO: public yn
	private Restaurant getRestaurantById(Long restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new GeneralException(ResponseCode.BAD_REQUEST));
	}

	private User getUser(Long userId) {
		return User.builder().id(userId).build();
	}
}
