package com.toppings.server.domain.user.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.toppings.server.domain.user.constant.Auth;
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
public class UserResponse {

	private Long id;

	private String name;

	private String email;

	private String country;

	private List<UserHabitResponse> habits;

	private String profile;

	private Integer postCount;

	private Integer scrapCount;

	private Integer reviewCount;

	private Auth role;

	private String habitContents;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
	private LocalDateTime createDate;

	public static UserResponse entityToDto(User user) {
		return UserResponse.builder()
			.id(user.getId())
			.name(user.getName())
			.email(user.getEmail())
			.country(user.getCountry())
			.profile(user.getProfile())
			.build();
	}

	public void updateCount(UserCount userCount) {
		this.postCount = userCount.getPostCount();
		this.scrapCount = userCount.getScrapCount();
		this.reviewCount = userCount.getReviewCount();
	}

	public void updateHabits(List<UserHabitResponse> habits) {
		if (habits != null && !habits.isEmpty())
			this.habits = habits;
	}
}