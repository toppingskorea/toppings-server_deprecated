package com.toppings.server.domain.recent.repository;

import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.dto.RecentResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QueryDslRecentRepository {

    List<RecentResponse> getRecents(RecentType type, Long id);
}
