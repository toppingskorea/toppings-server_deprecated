package com.toppings.server.domain.restaurant.dto;

import javax.validation.constraints.NotNull;

import com.toppings.server.domain.restaurant.constant.SearchType;
import com.toppings.server.domain.user.constant.Habit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantFilterSearchRequest {

	@NotNull(message = "검색 유형을 확인해주세요.")
	private SearchType type;

	private String name;

	private String country;

	private Habit habit;

	private Double x1;

	private Double x2;

	private Double y1;

	private Double y2;

	public boolean isValidPoint() {
		return !this.type.equals(SearchType.Name)
			&& (this.getX1() == null || this.getX2() == null || this.getY1() == null || this.getY2() == null);
	}
}
