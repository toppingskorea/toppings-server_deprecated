package com.toppings.server.domain.scrap.repository;

import static com.toppings.server.domain.scrap.entity.QScrap.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslScrapRepositoryImpl implements QueryDslScrapRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<RestaurantListResponse> findRestaurantByUserForScrap(Long userId) {
		return queryFactory.select(getFields())
			.from(scrap)
			.leftJoin(scrap.restaurant)
			.leftJoin(scrap.user)
			.where(eqUserId(userId)) // TODO: public yn
			.orderBy(scrap.restaurant.likeCount.desc())
			.fetch();
	}

	private BooleanExpression eqUserId(Long userId) {
		return scrap.user.id.eq(userId);
	}

	private QBean<RestaurantListResponse> getFields() {
		return Projections.fields(RestaurantListResponse.class, scrap.restaurant.id, scrap.restaurant.name,
			scrap.restaurant.address, scrap.restaurant.latitude, scrap.restaurant.longitude,
			scrap.restaurant.description, scrap.restaurant.type, scrap.restaurant.thumbnail, scrap.restaurant.likeCount,
			scrap.user.name.as("writer"));
	}
}
