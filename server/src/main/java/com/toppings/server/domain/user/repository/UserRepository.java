package com.toppings.server.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.server.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findUserByUsernameAndDeleteYn(
		String username,
		String deleteYn
	);

	Optional<User> findUserByUsername(String username);
}
