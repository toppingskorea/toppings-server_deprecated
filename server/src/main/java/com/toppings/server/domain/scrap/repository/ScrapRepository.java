package com.toppings.server.domain.scrap.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.scrap.entity.Scrap;
import com.toppings.server.domain.user.entity.User;

public interface ScrapRepository extends JpaRepository<Scrap, String>, QueryDslScrapRepository {

	Optional<Scrap> findScrapByRestaurantAndUser(
		Restaurant restaurant,
		User user
	);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM Scrap s WHERE s.restaurant = :restaurant")
	void deleteBatchByRestaurant(Restaurant restaurant);
}
