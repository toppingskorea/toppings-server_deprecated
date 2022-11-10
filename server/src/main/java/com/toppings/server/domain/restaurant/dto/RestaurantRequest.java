package com.toppings.server.domain.restaurant.dto;

import com.toppings.server.domain.restaurant.constant.FoodType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantRequest {

	private String name;

	private String description;

	private String address;

	private String zipcode;

	private String latitude;

	private String longitude;

	private FoodType type;

}
