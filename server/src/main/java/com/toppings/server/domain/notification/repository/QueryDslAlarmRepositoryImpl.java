package com.toppings.server.domain.notification.repository;

import static com.toppings.server.domain.notification.entity.QAlarm.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.server.domain.notification.dto.AlarmResponse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslAlarmRepositoryImpl implements QueryDslAlarmRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<AlarmResponse> findAlarms(Long userId) {
		return queryFactory.select(
			Projections.fields(AlarmResponse.class, alarm.id, alarm.user.name.as("userName"), alarm.user.country,
				alarm.content, alarm.alarmType, alarm.restaurant.name.as("restaurantName"), alarm.restaurant.thumbnail))
			.from(alarm)
			.leftJoin(alarm.user)
			.leftJoin(alarm.restaurant)
			.where(alarm.user.id.eq(userId))
			.orderBy(alarm.id.desc())
			.fetch();
	}
}
