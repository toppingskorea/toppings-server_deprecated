package com.toppings.server.domain.scrap.repository;

import java.util.List;
import java.util.Optional;

import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.scrap.entity.Scrap;
import com.toppings.server.domain.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long>, QueryDslScrapRepository {

	Optional<Scrap> findScrapByRestaurantAndUser(
		Restaurant restaurant,
		User user
	);
}
