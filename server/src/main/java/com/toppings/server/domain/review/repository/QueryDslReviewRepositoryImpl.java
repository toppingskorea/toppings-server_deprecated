package com.toppings.server.domain.review.repository;

import static com.toppings.server.domain.review.entity.QReview.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.server.domain.review.dto.ReviewResponse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslReviewRepositoryImpl implements QueryDslReviewRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<ReviewResponse> findReviewByRestaurantId(
		Long restaurantId,
		Long userId
	) {
		return queryFactory.select(
			Projections.fields(ReviewResponse.class, review.id, review.description, review.images,
				review.updateDate.as("modifiedAt"), review.user.name, review.user.country, review.user.habits,
				review.user.id.eq(userId).as("isMine")
			))
			.from(review)
			.where(review.restaurant.id.eq(restaurantId))
			.orderBy(review.updateDate.desc())
			.fetch();
	}
}
