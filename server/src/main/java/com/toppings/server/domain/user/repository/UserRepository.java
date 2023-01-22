package com.toppings.server.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toppings.server.domain.user.constant.Auth;
import com.toppings.server.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findUserByUsernameAndDeleteYn(
		String username,
		String deleteYn
	);

	Optional<User> findUserByUsername(String username);

	Long countByRole(Auth role);
}
