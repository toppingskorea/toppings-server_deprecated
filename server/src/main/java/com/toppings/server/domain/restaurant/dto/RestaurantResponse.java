package com.toppings.server.domain.restaurant.dto;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

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
public class RestaurantResponse {

	private Long id;

	private String name;

	private String address;

	private Double longitude;

	private Double latitude;

	private String description;

	private FoodType type;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name = "t_eating_habit",
		joinColumns = @JoinColumn(name = "user_id")
	)
	@Column(name = "user_habit", columnDefinition = "varchar(100)")
	private List<String> images;

	private String code;

	// 좋아요 갯수

	// 스크랩 갯수

	// 작성자

	// 국적

	// 식습관

	public static RestaurantResponse entityToDto(Restaurant restaurant) {
		return RestaurantResponse.builder()
			.id(restaurant.getId())
			.name(restaurant.getName())
			.type(restaurant.getType())
			.address(restaurant.getAddress())
			.description(restaurant.getDescription())
			.latitude(restaurant.getLatitude())
			.longitude(restaurant.getLongitude())
			.code(restaurant.getCode())
			.images(restaurant.getImages())
			.build();
	}
}
