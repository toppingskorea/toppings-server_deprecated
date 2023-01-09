package com.toppings.server.domain.user.repository;

import java.util.Optional;

import com.toppings.server.domain.user.dto.UserCount;
import com.toppings.server.domain.user.entity.User;

public interface QueryDslUserRepository {

	Optional<User> getUserResponseById(Long userId);

	public UserCount getUserCount(Long userId);
}
