package com.toppings.server.domain.review.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;
import com.toppings.server.domain.review.dto.ReviewListResponse;

public interface QueryDslReviewRepository {

	List<ReviewListResponse> findReviewByRestaurantId(
		Long restaurantId,
		Long userId
	);

	List<RestaurantListResponse> findRestaurantByUserForReview(Long userId);

	Page<ReviewListResponse> findAllForAdmin(Pageable pageable);
}
