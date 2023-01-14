package com.toppings.server.domain.restaurant.repository;

import static com.toppings.server.domain.restaurant.entity.QRestaurant.*;
import static org.springframework.util.StringUtils.*;

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
import com.toppings.server.domain.restaurant.dto.RestaurantMapSearchRequest;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslRestaurantRepositoryImpl implements QueryDslRestaurantRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<RestaurantListResponse> findAllBySearchForMap(RestaurantMapSearchRequest searchRequest) {
		return queryFactory.select(getFields())
			.from(restaurant)
			.leftJoin(restaurant.user)
			.where(
				restaurant.latitude.gt(searchRequest.getX1()),
				restaurant.latitude.lt(searchRequest.getX2()),
				restaurant.longitude.gt(searchRequest.getY1()),
				restaurant.longitude.lt(searchRequest.getY2()),
				notEqPublicYn()
			)
			.orderBy(restaurant.likeCount.desc())
			.fetch();
	}

	private BooleanExpression notEqPublicYn() {
		return restaurant.publicYn.ne("N");
	}

	@Override
	public List<RestaurantListResponse> findAllByRestaurantName(String name) {
		return queryFactory.select(getFields())
			.from(restaurant)
			.leftJoin(restaurant.user)
			.where(eqName(name), notEqPublicYn())
			.orderBy(restaurant.likeCount.desc())
			.fetch();
	}

	@Override
	public List<RestaurantListResponse> findRestaurantByUser(Long userId) {
		return queryFactory.select(getFields())
			.from(restaurant)
			.leftJoin(restaurant.user)
			.where(eqUserId(userId), notEqPublicYn())
			.orderBy(restaurant.likeCount.desc())
			.fetch();
	}

	@Override
	public Page<RestaurantListResponse> findAllForAdmin(Pageable pageable) {
		List<RestaurantListResponse> restaurantListResponses = queryFactory.select(getFields())
			.from(restaurant)
			.leftJoin(restaurant.user)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(restaurant.id.desc())
			.fetch();

		Long totalCount = queryFactory.select(Wildcard.count).from(restaurant).fetch().get(0);
		return new PageWrapper<>(restaurantListResponses, pageable.getPageNumber(), pageable.getPageSize(), totalCount);
	}

	private BooleanExpression eqUserId(Long userId) {
		return restaurant.user.id.eq(userId);
	}

	private QBean<RestaurantListResponse> getFields() {
		return Projections.fields(RestaurantListResponse.class, restaurant.id, restaurant.name, restaurant.address,
			restaurant.latitude, restaurant.longitude, restaurant.description, restaurant.type,
			restaurant.thumbnail, restaurant.likeCount, restaurant.user.name.as("writer"), restaurant.user.country,
			restaurant.createDate, restaurant.publicYn);
	}

	private BooleanExpression inIds(List<Long> ids) {
		return ids.size() > 0 ? restaurant.id.in(ids) : null;
	}

	private BooleanExpression eqName(String name) {
		return hasText(name) ? restaurant.name.contains(name) : null;
	}
}
