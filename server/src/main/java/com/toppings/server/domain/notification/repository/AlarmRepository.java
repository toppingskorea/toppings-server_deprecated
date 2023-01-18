package com.toppings.server.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.server.domain.notification.entity.Alarm;
import com.toppings.server.domain.restaurant.entity.Restaurant;
import com.toppings.server.domain.review.entity.Review;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, QueryDslAlarmRepository {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM Alarm a WHERE a.restaurant = :restaurant")
	void deleteBatchByRestaurant(Restaurant restaurant);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM Alarm a WHERE a.review = :review")
	void deleteBatchByReview(Review review);
}
