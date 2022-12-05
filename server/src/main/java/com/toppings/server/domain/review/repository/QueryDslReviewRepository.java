package com.toppings.server.domain.review.repository;

import java.util.List;

import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;
import com.toppings.server.domain.review.dto.ReviewListResponse;

public interface QueryDslReviewRepository {

	List<ReviewListResponse> findReviewByRestaurantId(
		Long restaurantId,
		Long userId
	);

	List<RestaurantListResponse> findRestaurantByUserForReview(Long userId);
}
