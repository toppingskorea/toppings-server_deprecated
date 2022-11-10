package com.toppings.server.domain.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantSearchRequest {

	private String x1;

	private String x2;

	private String y1;

	private String y2;

	private String country;

	private String habit;

	private String name;
}
