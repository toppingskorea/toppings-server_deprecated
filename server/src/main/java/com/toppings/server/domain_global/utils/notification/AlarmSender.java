package com.toppings.server.domain_global.utils.notification;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.toppings.server.domain.notification.dto.AlarmResponse;
import com.toppings.server.domain.notification.entity.Alarm;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AlarmSender {

	private final SimpMessagingTemplate template;

	public void send(
		final Restaurant restaurant,
		final User user,
		final Alarm alarm,
		final String message
	) {
		final AlarmResponse alarmResponse = AlarmResponse.of(restaurant, user, alarm, message);
		template.convertAndSend("/sub/" + user.getId(), alarmResponse);
	}
}
