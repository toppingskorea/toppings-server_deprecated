package com.toppings.server.domain.user.repository;

import java.util.List;

import com.toppings.server.domain.user.dto.AdminResponse;

public interface QueryDslUserRepository {

	List<AdminResponse> findAdminAll();
}
