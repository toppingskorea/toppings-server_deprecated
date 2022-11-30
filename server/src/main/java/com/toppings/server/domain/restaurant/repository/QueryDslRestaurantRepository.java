package com.toppings.server.domain.restaurant.repository;

import java.util.List;

import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;
import com.toppings.server.domain.restaurant.dto.RestaurantSearchRequest;

public interface QueryDslRestaurantRepository {

	List<RestaurantListResponse> findAllBySearchForMap(
		RestaurantSearchRequest restaurantSearchRequest
	);

	List<RestaurantListResponse> findAllByRestaurantName(String name);

	List<RestaurantListResponse> findRestaurantByUser(Long userId);
}
