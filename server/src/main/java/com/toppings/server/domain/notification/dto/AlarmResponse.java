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

	private Long id;

	private String userName;

	private String country;

	private String content;

	private AlarmType alarmType;

	private String restaurantName;

	private String thumbnail;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH:mm")
	private LocalDateTime createDate;

	public static AlarmResponse of(
		Restaurant restaurant,
		User user,
		Alarm alarm
	) {
		return AlarmResponse.builder()
			.id(alarm.getId())
			.alarmType(alarm.getAlarmType())
			.content(alarm.getContent())
			.country(user != null ? user.getCountry() : null)
			.userName(user != null ? user.getName() : null)
			.restaurantName(restaurant.getName())
			.thumbnail(restaurant.getThumbnail())
			.createDate(alarm.getCreateDate())
			.build();
	}

	public static AlarmResponse of(
		Review review,
		User user,
		Alarm alarm
	) {
		return AlarmResponse.builder()
			.id(alarm.getId())
			.alarmType(alarm.getAlarmType())
			.content(alarm.getContent())
			.country(user != null ? user.getCountry() : null)
			.userName(user != null ? user.getName() : null)
			.thumbnail(review.getThumbnail())
			.createDate(alarm.getCreateDate())
			.build();
	}
}
