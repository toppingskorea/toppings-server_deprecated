package com.toppings.server.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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

	private String name;

	private String country;

	private String habit;

	public static UserResponse entityToDto(User user) {
		return UserResponse.builder()
			.name(user.getName())
			.country(user.getCountry())
			.habit(user.getHabit())
			.build();
	}
}
