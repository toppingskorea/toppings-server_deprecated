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

	private String message;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
	private LocalDateTime createDate;

	public static AlarmResponse of(
		Restaurant restaurant,
		User user,
		Alarm alarm,
		String message
	) {
		return AlarmResponse.builder()
			.id(alarm.getId())
			.alarmType(alarm.getAlarmType())
			.content(alarm.getContent())
			.country(user.getCountry())
			.userName(user.getName())
			.restaurantName(restaurant.getName())
			.thumbnail(restaurant.getThumbnail())
			.message(message)
			.createDate(alarm.getCreateDate())
			.build();
	}
}
