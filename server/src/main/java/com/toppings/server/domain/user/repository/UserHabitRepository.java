package com.toppings.server.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toppings.server.domain.user.entity.UserHabit;

public interface UserHabitRepository extends JpaRepository<UserHabit, Long> {
}
