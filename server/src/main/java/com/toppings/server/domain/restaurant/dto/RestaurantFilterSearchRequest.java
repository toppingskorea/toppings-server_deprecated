package com.toppings.server.domain.restaurant.dto;

import javax.validation.constraints.NotNull;

import com.toppings.server.domain.restaurant.constant.SearchType;
import com.toppings.server.domain.user.constant.Habit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RestaurantFilterSearchRequest extends RestaurantMapSearchRequest {

	@NotNull(message = "검색 유형을 확인해주세요.")
	private SearchType type;

	private String name;

	private String country;

	private Habit habit;
}
