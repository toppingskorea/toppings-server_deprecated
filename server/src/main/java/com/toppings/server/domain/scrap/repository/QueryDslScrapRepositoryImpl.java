package com.toppings.server.domain.scrap.repository;

import static com.toppings.server.domain.restaurant.entity.QRestaurant.*;
import static com.toppings.server.domain.scrap.entity.QScrap.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.common.dto.PageWrapper;
import com.toppings.server.domain.restaurant.dto.RestaurantListResponse;
import com.toppings.server.domain.user.constant.Auth;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslScrapRepositoryImpl implements QueryDslScrapRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<RestaurantListResponse> findRestaurantByUserForScrap(
		Long userId,
		Pageable pageable
	) {
		List<RestaurantListResponse> restaurantListResponses = queryFactory.select(getFields())
			.from(scrap)
			.leftJoin(scrap.restaurant.user)
			.where(eqUserId(userId), notEqPublicYn(), notMine(userId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(scrap.createDate.desc())
			.fetch();

		Long totalCount = queryFactory.select(Wildcard.count)
			.from(scrap)
			.leftJoin(scrap.restaurant)
			.where(eqUserId(userId), notEqPublicYn(), notMine(userId))
			.fetch()
			.get(0);

		return new PageWrapper<>(restaurantListResponses, pageable.getPageNumber(), pageable.getPageSize(), totalCount);
	}

	private BooleanExpression notMine(Long userId) {
		return scrap.restaurant.user.id.ne(userId);
	}

	@Override
	public Integer findRestaurantScrapCountByUser(Long userId) {
		return Math.toIntExact(queryFactory.select(Wildcard.count)
			.from(scrap)
			.leftJoin(scrap.restaurant)
			.where(eqUserId(userId), notEqPublicYn(), notMine(userId))
			.fetch().get(0));
	}

	private BooleanExpression notEqPublicYn() {
		return scrap.restaurant.publicYn.ne("N");
	}

	private BooleanExpression eqUserId(Long userId) {
		return scrap.user.id.eq(userId);
	}

	private QBean<RestaurantListResponse> getFields() {
		return Projections.fields(RestaurantListResponse.class, scrap.restaurant.id, scrap.restaurant.name,
			scrap.restaurant.address, scrap.restaurant.latitude, scrap.restaurant.longitude,
			scrap.restaurant.description, scrap.restaurant.type, scrap.restaurant.thumbnail, scrap.restaurant.likeCount,
			scrap.restaurant.user.name.as("writer"), scrap.restaurant.user.role.eq(Auth.ROLE_ADMIN).as("isAdmin"));
	}
}
