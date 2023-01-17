package com.toppings.server.domain.notification.dto;

import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmRequest {

	private Restaurant restaurant;

	private Review review;

	private AlarmType type;

	private String content;

	public static AlarmRequest of(
		Restaurant restaurant,
		AlarmType type,
		String content
	) {
		return AlarmRequest.builder()
			.restaurant(restaurant)
			.type(type)
			.content(content)
			.build();
	}

	public static AlarmRequest of(
		Review review,
		AlarmType type,
		String content
	) {
		return AlarmRequest.builder()
			.review(review)
			.type(type)
			.content(content)
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
