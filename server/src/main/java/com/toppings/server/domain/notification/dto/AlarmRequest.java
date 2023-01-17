package com.toppings.server.domain.notification.dto;

import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.review.entity.Review;
import com.toppings.server.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmRequest {

	private User fromUser;

	private Restaurant restaurant;

	private Review review;

	private AlarmType type;

	private String content;

	public static AlarmRequest of(
		User fromUser,
		Restaurant restaurant,
		AlarmType type,
		String content
	) {
		return AlarmRequest.builder()
			.fromUser(fromUser)
			.restaurant(restaurant)
			.type(type)
			.content(content)
			.build();
	}

	public static AlarmRequest of(
		User fromUser,
		Review review,
		AlarmType type,
		String content
	) {
		return AlarmRequest.builder()
			.fromUser(fromUser)
			.review(review)
			.type(type)
			.content(content)
			.build();
	}

	public static AlarmRequest of(
		User fromUser,
		Restaurant restaurant,
		AlarmType type
	) {
		return AlarmRequest.builder()
			.fromUser(fromUser)
			.restaurant(restaurant)
			.type(type)
			.build();
	}

	public static AlarmRequest of(
		Restaurant restaurant,
		AlarmType type
	) {
		return AlarmRequest.builder()
			.restaurant(restaurant)
			.type(type)
			.build();
	}
}
