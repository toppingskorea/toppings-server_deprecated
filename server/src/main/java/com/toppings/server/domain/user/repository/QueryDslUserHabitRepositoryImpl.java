package com.toppings.server.domain.user.repository;

import static com.toppings.server.domain.user.entity.QUserHabit.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.server.domain.restaurant.dto.RestaurantSearchRequest;
import com.toppings.server.domain.user.constant.Habit;
import com.toppings.server.domain.user.constant.HabitTitle;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslUserHabitRepositoryImpl implements QueryDslUserHabitRepository {

	private final JPAQueryFactory queryFactory;

	public List<Long> findUserIdByHabit(RestaurantSearchRequest searchRequest) {
		return queryFactory.select(userHabit.user.id)
			.from(userHabit)
			.leftJoin(userHabit.user)
			.where(userHabit.title.eq(searchRequest.getHabitTitle()), userHabit.content.eq(searchRequest.getHabit()))
			.fetch();
	}
}
