package com.toppings.server.domain.recent.repository;

import java.util.List;
import java.util.Optional;

import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.dto.RecentRequest;
import com.toppings.server.domain.recent.dto.RecentResponse;
import com.toppings.server.domain.recent.entity.Recent;
import com.toppings.server.domain.user.entity.User;

public interface QueryDslRecentRepository {

	List<RecentResponse> findRecents(
		RecentType type,
		Long id
	);
}
