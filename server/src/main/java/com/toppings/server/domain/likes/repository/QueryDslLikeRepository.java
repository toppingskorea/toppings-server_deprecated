package com.toppings.server.domain.likes.repository;

import java.util.List;

import com.toppings.server.domain.likes.dto.LikesPercent;
import com.toppings.server.domain.restaurant.dto.RestaurantFilterSearchRequest;
import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;

public interface QueryDslLikeRepository {

	List<RestaurantListResponse> findRestaurantsByUserCountry(RestaurantFilterSearchRequest country);

	List<RestaurantListResponse> findRestaurantsByUserHabit(
		List<Long> ids,
		RestaurantFilterSearchRequest searchRequest
	);

	List<LikesPercent> findLikesPercentForCountry(Long restaurantId);

	List<LikesPercent> findLikesPercentForHabit(Long restaurantId);
}
