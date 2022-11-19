package com.toppings.server.domain.restaurant.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslRestaurantRepositoryImpl implements QueryDslRestaurantRepository {

	private final JPAQueryFactory queryFactory;
}
