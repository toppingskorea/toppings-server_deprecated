package com.toppings.server.domain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toppings.server.domain.notification.entity.Alarm;

public interface AlarmRepository extends JpaRepository<Alarm, Long>, QueryDslAlarmRepository {
}
