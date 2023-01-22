package com.toppings.server.domain.scrap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;

public interface QueryDslScrapRepository {

	Page<RestaurantListResponse> findRestaurantByUserForScrap(
		Long userId,
		Pageable pageable
	);

	Integer findRestaurantScrapCountByUser(Long userId);
}
