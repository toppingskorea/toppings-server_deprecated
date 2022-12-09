package com.toppings.server.domain.notification.repository;

import java.util.List;

import com.toppings.server.domain.notification.dto.AlarmResponse;

public interface QueryDslAlarmRepository {

	List<AlarmResponse> findAlarms(Long userId);
}
