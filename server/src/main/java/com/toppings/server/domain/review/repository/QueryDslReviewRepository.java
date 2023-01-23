package com.toppings.server.domain.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;
import com.toppings.server.domain.review.dto.ReviewListResponse;

public interface QueryDslReviewRepository {

	Page<ReviewListResponse> findReviewByRestaurantId(
		Long restaurantId,
		Long userId,
		Pageable pageable
	);

	Page<RestaurantListResponse> findRestaurantByUserForReview(
		Long userId,
		Pageable pageable
	);

	Integer findRestaurantReviewCountByUser(Long userId);

	Page<ReviewListResponse> findAllForAdmin(Pageable pageable);
}
