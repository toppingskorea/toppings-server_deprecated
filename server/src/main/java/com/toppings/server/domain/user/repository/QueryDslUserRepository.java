package com.toppings.server.domain.user.repository;

import com.toppings.server.domain.user.dto.UserCount;

public interface QueryDslUserRepository {

	UserCount getUserCount(Long userId);
}
