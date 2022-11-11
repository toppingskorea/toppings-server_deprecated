package com.toppings.server.domain.recent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toppings.server.domain.recent.entity.Recent;
import com.toppings.server.domain.user.entity.User;

public interface RecentRepository extends JpaRepository<Recent, Long>, QueryDslRecentRepository {

	void deleteAllByUser(User user);
}
