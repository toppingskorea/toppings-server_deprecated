package com.toppings.server.domain.user.dto;

import java.util.List;

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
public class UserModifyRequest {

	private String name;

	private String country;

	private List<UserHabitRequest> habit;

	private String profile;

	public static void modifyUserInfo(UserModifyRequest request, User user) {
		user.setName(request.getName() != null ? request.getName() : user.getName());
		user.setCountry(
			request.getCountry() != null ? request.getCountry() : user.getCountry());
	}
}
