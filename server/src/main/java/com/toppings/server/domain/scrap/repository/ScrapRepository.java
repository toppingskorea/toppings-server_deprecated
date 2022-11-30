package com.toppings.server.domain.scrap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.scrap.entity.Scrap;
import com.toppings.server.domain.user.entity.User;

public interface ScrapRepository extends JpaRepository<Scrap, Long>, QueryDslScrapRepository {

	Optional<Scrap> findScrapByRestaurantAndUser(
		Restaurant restaurant,
		User user
	);
}
