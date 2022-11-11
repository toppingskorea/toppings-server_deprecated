package com.toppings.server.domain.user.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toppings.server.domain.user.constant.Habit;
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

	private String country;

	private List<Habit> habits;

	public static UserResponse entityToDto(User user) {
		return UserResponse.builder()
			.id(user.getId())
			.name(user.getName())
			.country(user.getCountry())
			.habits(user.getHabits())
			.build();
	}
}