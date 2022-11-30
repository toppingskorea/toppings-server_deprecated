package com.toppings.server.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toppings.server.domain.user.constant.Habit;
import com.toppings.server.domain.user.constant.HabitTitle;
import com.toppings.server.domain.user.entity.UserHabit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserHabitResponse {

	private HabitTitle title;

	private Habit content;

	public static UserHabitResponse entityToDto(UserHabit userHabit) {
		return UserHabitResponse.builder()
			.title(userHabit.getTitle())
			.content(userHabit.getContent())
			.build();
	}
}
