package com.toppings.server.domain.restaurant.dto;

import javax.validation.constraints.NotNull;

import com.toppings.server.domain.restaurant.constant.SearchType;
import com.toppings.server.domain.user.constant.Habit;
import com.toppings.server.domain.user.constant.HabitTitle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantSearchRequest {

	@NotNull(message = "검색 유형을 확인해주세요.")
	private SearchType type;

	private Double x1;

	private Double x2;

	private Double y1;

	private Double y2;

	private String name;

	private String country;

	private Habit habit;
}
