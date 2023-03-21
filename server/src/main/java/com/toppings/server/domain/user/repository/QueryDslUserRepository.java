package com.toppings.server.domain.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.toppings.server.domain.user.dto.UserResponse;

public interface QueryDslUserRepository {

	Page<UserResponse> findAllUser(Pageable pageable);
}
