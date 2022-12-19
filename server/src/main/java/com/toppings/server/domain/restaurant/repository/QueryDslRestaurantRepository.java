package com.toppings.server.domain.restaurant.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;
import com.toppings.server.domain.restaurant.dto.RestaurantMapSearchRequest;

public interface QueryDslRestaurantRepository {

	List<RestaurantListResponse> findAllBySearchForMap(RestaurantMapSearchRequest searchRequest);

	List<RestaurantListResponse> findAllByRestaurantName(String name);

	List<RestaurantListResponse> findRestaurantByUser(Long userId);

	Page<RestaurantListResponse> findAllForAdmin(Pageable pageable);
}
