package com.toppings.server.domain.restaurant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toppings.server.domain.restaurant.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>, QueryDslRestaurantRepository {

	Optional<Restaurant> findRestaurantByCode(String code);

	Optional<Restaurant> findRestaurantByIdAndPublicYnNot(
		Long restaurantId,
		String publicYn
	);

	Long countByPublicYnNot(String publicYn);
}
