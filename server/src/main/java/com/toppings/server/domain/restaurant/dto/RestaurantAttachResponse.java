package com.toppings.server.domain.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.restaurant.entity.RestaurantAttach;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantAttachResponse {
	
	private String name;

	public static RestaurantAttachResponse dtoToEntity(RestaurantAttach restaurantAttach) {
		return RestaurantAttachResponse.builder()
			.name(restaurantAttach.getImage())
			.build();
	}
}
