package com.toppings.server.domain.user.repository;

import static com.toppings.server.domain.user.entity.QUser.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.server.domain.user.dto.UserCount;
import com.toppings.server.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslUserRepositoryImpl implements QueryDslUserRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Optional<User> getUserResponseById(Long userId) {
		return Optional.ofNullable(queryFactory.select(user)
			.from(user)
			.join(user.habits).fetchJoin()
			.where(user.id.eq(userId))
			.fetchOne()
		);
	}

	@Override
	public UserCount getUserCount(Long userId) {
		return queryFactory.select(
			Projections.fields(UserCount.class,
				user.restaurants.size().as("postCount"),
				user.scraps.size().as("scrapCount")))
			.from(user)
			.where(user.id.eq(userId))
			.fetchOne();
	}
}
