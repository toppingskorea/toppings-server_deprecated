package com.toppings.server.domain.user.dto;

import java.util.List;

import com.toppings.server.domain.user.constant.Habit;

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
}
