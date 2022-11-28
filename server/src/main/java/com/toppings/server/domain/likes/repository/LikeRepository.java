package com.toppings.server.domain.likes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toppings.server.domain.likes.entity.Likes;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.user.entity.User;

public interface LikeRepository extends JpaRepository<Likes, Long>, QueryDslLikeRepository {

	Optional<Likes> findLikesByRestaurantAndUser(
		Restaurant restaurant,
		User user
	);

	Optional<Likes> findLikesByUser(User user);
}
