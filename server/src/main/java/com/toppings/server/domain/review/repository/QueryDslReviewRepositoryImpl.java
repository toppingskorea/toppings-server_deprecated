package com.toppings.server.domain.review.repository;

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
	public Page<ReviewListResponse> findReviewByRestaurantId(
		Long restaurantId,
		Long userId,
		Pageable pageable
	) {
		List<ReviewListResponse> reviewListResponses = queryFactory.select(
			Projections.fields(ReviewListResponse.class, review.id, review.description, review.thumbnail,
				review.updateDate.as("modifiedAt"), review.user.name, review.user.country,
				getIsMine(userId).as("isMine"), review.user.habitContents, review.publicYn
			))
			.from(review)
			.innerJoin(review.user)
			.where(review.restaurant.id.eq(restaurantId), notEqPublicYn())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(review.updateDate.desc())
			.fetch();

		Long totalCount = queryFactory.select(Wildcard.count)
			.from(review)
			.where(review.restaurant.id.eq(restaurantId), notEqPublicYn())
			.fetch()
			.get(0);

		return new PageWrapper<>(reviewListResponses, pageable.getPageNumber(), pageable.getPageSize(), totalCount);
	}

	@Override
	public Page<RestaurantListResponse> findRestaurantByUserForReview(
		Long userId,
		Pageable pageable
	) {
		List<RestaurantListResponse> restaurantListResponses = queryFactory.select(getFields())
			.distinct()
			.from(review)
			.leftJoin(review.restaurant.user)
			.where(eqUserId(userId), notEqPublicYn())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(review.id.desc())
			.fetch();

		Long totalCount = queryFactory.select(Wildcard.count)
			.distinct()
			.from(review)
			.leftJoin(review.restaurant)
			.where(eqUserId(userId), notEqPublicYn())
			.fetch()
			.get(0);

		return new PageWrapper<>(restaurantListResponses, pageable.getPageNumber(), pageable.getPageSize(), totalCount);
	}

	@Override
	public Integer findRestaurantReviewCountByUser(Long userId) {
		return Math.toIntExact(queryFactory.select(Wildcard.count)
			.distinct()
			.from(review)
			.leftJoin(review.restaurant)
			.where(eqUserId(userId), notEqPublicYn())
			.fetch().get(0));
	}

	@Override
	public Page<ReviewListResponse> findAllForAdmin(Pageable pageable) {
		List<ReviewListResponse> reviewListResponses = queryFactory.select(
			Projections.fields(ReviewListResponse.class, review.id, review.description, review.thumbnail,
				review.updateDate.as("modifiedAt"), review.user.name, review.user.country, review.user.habitContents,
				review.publicYn, review.restaurant.name.as("restaurantName")))
			.from(review)
			.innerJoin(review.user)
			.leftJoin(review.restaurant)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(review.id.desc())
			.fetch();

		Long totalCount = queryFactory.select(Wildcard.count).from(review).fetch().get(0);

		return new PageWrapper<>(reviewListResponses, pageable.getPageNumber(), pageable.getPageSize(), totalCount);
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
			review.restaurant.likeCount, review.restaurant.user.name.as("writer"), review.restaurant.publicYn,
			review.id.as("reviewId"));
	}

	private BooleanExpression notEqPublicYn() {
		return review.restaurant.publicYn.ne("N");
	}
}
