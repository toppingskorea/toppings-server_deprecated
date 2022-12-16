package com.toppings.server.domain.restaurant.dto;

import javax.validation.constraints.NotNull;

import com.toppings.server.domain.restaurant.constant.SearchType;
import com.toppings.server.domain.user.constant.Habit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantFilterSearchRequest {

	@NotNull(message = "검색 유형을 확인해주세요.")
	private SearchType type;

	private String name;

	private String country;

	private Habit habit;
}
