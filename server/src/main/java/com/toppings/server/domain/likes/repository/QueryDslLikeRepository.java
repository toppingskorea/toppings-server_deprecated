package com.toppings.server.domain.likes.repository;

import java.util.List;

import com.toppings.server.domain.likes.dto.LikesPercent;
import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;

public interface QueryDslLikeRepository {

	List<RestaurantListResponse> findRestaurantIdByUserCountry(String country);

	List<RestaurantListResponse> findRestaurantIdByUserHabit(List<Long> ids);

	List<LikesPercent> findLikesPercentForCountry(Long restaurantId);

	List<LikesPercent> findLikesPercentForHabit(Long restaurantId);
}
