package com.toppings.server.domain.notification.repository;

import static com.toppings.server.domain.notification.entity.QAlarm.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.common.dto.PageWrapper;
import com.toppings.server.domain.notification.dto.AlarmResponse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslAlarmRepositoryImpl implements QueryDslAlarmRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<AlarmResponse> findAlarms(
		Long userId,
		Pageable pageable
	) {
		List<AlarmResponse> alarmResponses = queryFactory.select(
			Projections.fields(AlarmResponse.class, alarm.id, alarm.user.name.as("userName"), alarm.user.country,
				alarm.content, alarm.alarmType, alarm.restaurant.name.as("restaurantName"), alarm.restaurant.thumbnail))
			.from(alarm)
			.leftJoin(alarm.user)
			.leftJoin(alarm.restaurant)
			.where(alarm.user.id.eq(userId))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(alarm.id.desc())
			.fetch();

		Long totalCount = queryFactory.select(Wildcard.count)
			.from(alarm)
			.where(alarm.user.id.eq(userId))
			.fetch()
			.get(0);

		return new PageWrapper<>(alarmResponses, pageable.getPageNumber(), pageable.getPageSize(), totalCount);
	}
}
