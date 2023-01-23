package com.toppings.server.domain.likes.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.server.domain.likes.entity.Likes;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.review.entity.Review;
import com.toppings.server.domain.user.entity.User;

public interface LikeRepository extends JpaRepository<Likes, String>, QueryDslLikeRepository {

	Optional<Likes> findLikesByRestaurantAndUser(
		Restaurant restaurant,
		User user
	);

	List<Likes> findLikesByUser(User user);

	Long countByRestaurant(Restaurant restaurant);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM Likes l WHERE l.restaurant = :restaurant")
	void deleteBatchByRestaurant(Restaurant restaurant);
}
