package com.toppings.server.domain.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.review.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, QueryDslReviewRepository {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM Review r WHERE r.restaurant = :restaurant")
	void deleteBatchByRestaurant(Restaurant restaurant);

	Optional<Review> findReviewByIdAndPublicYnNot(
		Long reviewId,
		String publicYn
	);

	Long countBy();
}
