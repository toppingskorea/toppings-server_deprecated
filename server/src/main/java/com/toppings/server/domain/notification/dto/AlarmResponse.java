package com.toppings.server.domain.notification.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.toppings.server.domain.notification.constant.AlarmType;
import com.toppings.server.domain.notification.entity.Alarm;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmResponse {

	private Long restaurantId;

	private String userName;

	private String country;

	private String content;

	private AlarmType alarmType;

	private String restaurantName;

	private String thumbnail;

	private String profile;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
	private LocalDateTime createDate;

	public static AlarmResponse of(
		User user,
		Alarm alarm
	) {
		Restaurant restaurant = alarm.getRestaurant();
		Review review = alarm.getReview();
		String thumbnail = restaurant != null ? restaurant.getThumbnail() : review.getThumbnail();

		return AlarmResponse.builder()
			.restaurantId(alarm.getRestaurant().getId())
			.alarmType(alarm.getAlarmType())
			.content(alarm.getContent())
			.country(user.getCountry() == null ? user.getCountry() : null)
			.userName(user.getName())
			.profile(user.getProfile())
			.restaurantName(alarm.getRestaurant() != null ? alarm.getRestaurant().getName() : null)
			.thumbnail(thumbnail)
			.createDate(alarm.getCreateDate())
			.build();
	}
}
