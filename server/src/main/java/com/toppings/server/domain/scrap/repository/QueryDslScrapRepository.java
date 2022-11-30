package com.toppings.server.domain.scrap.repository;

import java.util.List;

import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;

public interface QueryDslScrapRepository {

	List<RestaurantListResponse> findRestaurantByUserForScrap(Long userId);
}
