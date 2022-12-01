package com.toppings.server.domain.likes.repository;

import static com.toppings.server.domain.likes.entity.QLikes.*;
import static com.toppings.server.domain.user.entity.QUser.*;
import static com.toppings.server.domain.user.entity.QUserHabit.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.server.domain.likes.dto.LikesPercent;
import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;
import com.toppings.server.domain_global.utils.OrderByNull;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslLikeRepositoryImpl implements QueryDslLikeRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<RestaurantListResponse> findRestaurantIdByUserCountry(String country) {
		return queryFactory.select(getFields())
			.distinct()
			.from(likes)
			.leftJoin(likes.restaurant)
			.leftJoin(likes.user)
			.where(likes.user.country.eq(country), likes.restaurant.likeCount.gt(0))
			.orderBy(likes.restaurant.likeCount.desc())
			.fetch();
	}

	@Override
	public List<RestaurantListResponse> findRestaurantIdByUserHabit(List<Long> ids) {
		return queryFactory.select(getFields())
			.distinct()
			.from(likes)
			.leftJoin(likes.restaurant)
			.leftJoin(likes.user)
			.where(likes.user.id.in(ids), likes.restaurant.likeCount.gt(0))
			.fetch();
	}

	@Override
	public List<LikesPercent> findLikesPercentForCountry(Long restaurantId) {
		return queryFactory.select(
			Projections.fields(LikesPercent.class, likes.count().as("count"), user.country))
			.from(user)
			.leftJoin(likes).on(user.id.eq(likes.user.id))
			.where(eqRestaurantId(restaurantId))
			.groupBy(user.country)
			.orderBy(likes.count().desc(), OrderByNull.DEFAULT)
			.fetch();
	}

	@Override
	public List<LikesPercent> findLikesPercentForHabit(Long restaurantId) {
		return queryFactory.select(
			Projections.fields(LikesPercent.class, likes.count().as("count"), userHabit.content.as("habit")))
			.from(user)
			.leftJoin(likes).on(user.id.eq(likes.user.id))
			.leftJoin(userHabit).on(user.id.eq(userHabit.user.id))
			.where(eqRestaurantId(restaurantId), userHabit.content.isNotNull())
			.groupBy(userHabit.content)
			.orderBy(likes.count().desc(), OrderByNull.DEFAULT)
			.fetch();
	}

	private BooleanExpression eqRestaurantId(Long restaurantId) {
		return likes.restaurant.id.eq(restaurantId);
	}

	private QBean<RestaurantListResponse> getFields() {
		return Projections.fields(RestaurantListResponse.class, likes.restaurant.id, likes.restaurant.name,
			likes.restaurant.address, likes.restaurant.latitude, likes.restaurant.longitude,
			likes.restaurant.description, likes.restaurant.type, likes.restaurant.thumbnail, likes.restaurant.likeCount,
			likes.user.name.as("writer"));
	}
}
