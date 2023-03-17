package com.toppings.planet.domain.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toppings.planet.domain.user.UserHabit;

public interface UserHabitJpaRepository extends JpaRepository<UserHabit, Long> {
}
