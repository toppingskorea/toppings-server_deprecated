package com.toppings.server.domain.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toppings.server.domain.restaurant.constant.FoodType;
import com.toppings.server.domain.restaurant.entity.Restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantListResponse {

	private Long id;

	private String name;

	private String address;

	private Double latitude;

	private Double longitude;

	private String description;

	private FoodType type;

	private String thumbnail;

	// 좋아요 갯수
	private Integer likeCount;

	// 작성자
	private String writer;

	private boolean isLike;

	public static RestaurantListResponse entityToDto(Restaurant restaurant) {
		return RestaurantListResponse.builder()
			.id(restaurant.getId())
			.name(restaurant.getName())
			.type(restaurant.getType())
			.address(restaurant.getAddress())
			.description(restaurant.getDescription())
			.latitude(restaurant.getLatitude())
			.longitude(restaurant.getLongitude())
			.build();
	}
}
