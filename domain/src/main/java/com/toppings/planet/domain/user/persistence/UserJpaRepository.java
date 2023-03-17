package com.toppings.planet.domain.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toppings.planet.domain.user.User;
import com.toppings.planet.domain.user.persistence.querydsl.QuerydslUserJpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long>, QuerydslUserJpaRepository {
}
