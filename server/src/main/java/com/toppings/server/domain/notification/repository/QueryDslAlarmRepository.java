package com.toppings.server.domain.notification.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.toppings.server.domain.notification.dto.AlarmResponse;

public interface QueryDslAlarmRepository {

	Page<AlarmResponse> findAlarms(
		Long userId,
		Pageable pageable
	);
}
