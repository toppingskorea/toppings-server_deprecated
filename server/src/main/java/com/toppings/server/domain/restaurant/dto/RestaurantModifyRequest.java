package com.toppings.server.domain.restaurant.dto;

import java.util.List;

import com.toppings.server.domain.restaurant.constant.FoodType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantModifyRequest {

	private String name;

	private String description;

	private String address;

	private String zipcode;

	private String code;

	private Double latitude;

	private Double longitude;

	private FoodType type;

	private List<String> images;
}
