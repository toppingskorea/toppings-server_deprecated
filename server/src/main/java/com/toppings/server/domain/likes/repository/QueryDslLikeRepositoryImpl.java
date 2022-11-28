package com.toppings.server.domain.likes.repository;

import static com.toppings.server.domain.likes.entity.QLikes.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;

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

	private QBean<RestaurantListResponse> getFields() {
		return Projections.fields(RestaurantListResponse.class, likes.restaurant.id, likes.restaurant.name,
			likes.restaurant.address, likes.restaurant.latitude, likes.restaurant.longitude,
			likes.restaurant.description, likes.restaurant.type, likes.restaurant.thumbnail, likes.restaurant.likeCount,
			likes.user.name.as("writer"));
	}
}
