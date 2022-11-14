package com.toppings.server.domain.likes.repository;

import java.util.Optional;

import com.toppings.server.domain.likes.entity.Likes;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {

	Optional<Likes> findLikesByRestaurantAndUser(
		Restaurant restaurant,
		User user
	);
}
