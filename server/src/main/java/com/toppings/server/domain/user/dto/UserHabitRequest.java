package com.toppings.server.domain.user.dto;

import com.toppings.server.domain.user.constant.Habit;
import com.toppings.server.domain.user.constant.HabitTitle;
import com.toppings.server.domain.user.entity.User;
import com.toppings.server.domain.user.entity.UserHabit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserHabitRequest {

	private Habit content;

	private HabitTitle title;

	public static UserHabit createUserHabit(UserHabitRequest request, User user) {
		return UserHabit.builder()
			.content(request.getContent())
			.title(request.getTitle())
			.user(user)
			.build();
	}
}
