package com.toppings.server.domain.review.repository;

import static com.toppings.server.domain.restaurant.entity.QRestaurant.*;
import static com.toppings.server.domain.review.entity.QReview.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.common.dto.PageWrapper;
import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;
import com.toppings.server.domain.review.dto.ReviewListResponse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslReviewRepositoryImpl implements QueryDslReviewRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<ReviewListResponse> findReviewByRestaurantId(
		Long restaurantId,
		Long userId
	) {
		return queryFactory.select(
			Projections.fields(ReviewListResponse.class, review.id, review.description, review.thumbnail,
				review.updateDate.as("modifiedAt"), review.user.name, review.user.country,
				getIsMine(userId).as("isMine"), review.user.habitContents, review.publicYn
			))
			.from(review)
			.innerJoin(review.user)
			.where(review.restaurant.id.eq(restaurantId), notEqPublicYn())
			.orderBy(review.updateDate.desc())
			.fetch();
	}

	@Override
	public List<RestaurantListResponse> findRestaurantByUserForReview(Long userId) {
		return queryFactory.select(getFields())
			.distinct()
			.from(review)
			.leftJoin(review.restaurant)
			.leftJoin(review.user)
			.where(eqUserId(userId))
			.orderBy(review.restaurant.likeCount.desc())
			.fetch();
	}

	@Override
	public Page<ReviewListResponse> findAllForAdmin(Pageable pageable) {
		List<ReviewListResponse> reviewListResponses = getReviewListResponses(pageable);
		Long totalCount = queryFactory.select(Wildcard.count).from(review).fetch().get(0);
		return new PageWrapper<>(reviewListResponses, pageable.getPageNumber(), pageable.getPageSize(), totalCount);
	}

	private List<ReviewListResponse> getReviewListResponses(Pageable pageable) {
		return queryFactory.select(
			Projections.fields(ReviewListResponse.class, review.id, review.description, review.thumbnail,
				review.updateDate.as("modifiedAt"), review.user.name, review.user.country, review.user.habitContents,
				review.publicYn))
			.from(review)
			.innerJoin(review.user)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(review.id.desc())
			.fetch();
	}

	private BooleanExpression eqUserId(Long userId) {
		return review.user.id.eq(userId);
	}

	private BooleanExpression getIsMine(Long userId) {
		return userId != null ? eqUserId(userId) : Expressions.asBoolean(false);
	}

	private QBean<RestaurantListResponse> getFields() {
		return Projections.fields(RestaurantListResponse.class, review.restaurant.id, review.restaurant.name,
			review.restaurant.address, review.restaurant.latitude, review.restaurant.longitude,
			review.restaurant.description, review.restaurant.type, review.restaurant.thumbnail,
			review.restaurant.likeCount, review.user.name.as("writer"));
	}

	private BooleanExpression notEqPublicYn() {
		return review.publicYn.ne("N");
	}
}
