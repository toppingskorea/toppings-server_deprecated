package com.toppings.server.domain.likes.repository;

import static com.toppings.server.domain.likes.entity.QLikes.*;
import static com.toppings.server.domain.restaurant.entity.QRestaurant.*;
import static com.toppings.server.domain.user.entity.QUser.*;
import static com.toppings.server.domain.user.entity.QUserHabit.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.server.domain.likes.dto.FilterLikesCount;
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
		List<FilterLikesCount> filterLikesCount = queryFactory.select(
			Projections.fields(FilterLikesCount.class, likes.restaurant.id.as("restaurantId"),
				Wildcard.count.as("filterLikeCount")))
			.from(likes)
			.leftJoin(likes.restaurant)
			.leftJoin(likes.user)
			.where(likes.user.country.eq(country))
			.groupBy(likes.restaurant.id)
			.orderBy(OrderByNull.DEFAULT)
			.fetch();

		return getRestaurantListResponses(filterLikesCount);
	}

	@Override
	public List<RestaurantListResponse> findRestaurantIdByUserHabit(List<Long> ids) {
		List<FilterLikesCount> filterLikesCount = queryFactory.select(
			Projections.fields(FilterLikesCount.class, likes.restaurant.id.as("restaurantId"),
				Wildcard.count.as("filterLikeCount")))
			.from(likes)
			.leftJoin(likes.restaurant)
			.leftJoin(likes.user)
			.where(likes.user.id.in(ids))
			.groupBy(likes.restaurant.id)
			.orderBy(OrderByNull.DEFAULT)
			.fetch();

		return getRestaurantListResponses(filterLikesCount);
	}

	private List<RestaurantListResponse> getRestaurantListResponses(List<FilterLikesCount> filterLikesCount) {
		Map<Long, Long> longMap = getLongMap(filterLikesCount);

		List<RestaurantListResponse> restaurantListResponses = queryFactory.select(getFields())
			.from(restaurant)
			.leftJoin(restaurant.user)
			.where(restaurant.id.in(longMap.keySet()), restaurant.likeCount.gt(0))
			.orderBy(restaurant.likeCount.desc())
			.fetch();

		setFilterLikesCount(longMap, restaurantListResponses);
		return restaurantListResponses;
	}

	private void setFilterLikesCount(
		Map<Long, Long> longMap,
		List<RestaurantListResponse> restaurantListResponses
	) {
		restaurantListResponses.forEach(res -> {
			res.setFilterLikeCount(longMap.get(res.getId()));
		});
	}

	private Map<Long, Long> getLongMap(List<FilterLikesCount> filterLikesCount) {
		return filterLikesCount.stream()
			.collect(Collectors.toMap(FilterLikesCount::getRestaurantId, FilterLikesCount::getFilterLikeCount));
	}

	private QBean<RestaurantListResponse> getFields() {
		return Projections.fields(RestaurantListResponse.class, restaurant.id, restaurant.name,
			restaurant.address, restaurant.latitude, restaurant.longitude,
			restaurant.description, restaurant.type, restaurant.thumbnail, restaurant.likeCount,
			restaurant.user.name.as("writer"));
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
}
