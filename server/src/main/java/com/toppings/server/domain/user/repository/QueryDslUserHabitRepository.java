package com.toppings.server.domain.user.repository;

import java.util.List;

import com.toppings.server.domain.restaurant.dto.RestaurantFilterSearchRequest;

public interface QueryDslUserHabitRepository {

	List<Long> findUserIdByHabit(RestaurantFilterSearchRequest searchRequest);
}
