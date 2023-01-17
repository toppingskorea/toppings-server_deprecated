package com.toppings.server.domain.recent.repository;

import static com.toppings.server.domain.recent.entity.QRecent.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.common.dto.PageWrapper;
import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.dto.RecentResponse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslRecentRepositoryImpl implements QueryDslRecentRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<RecentResponse> findRecents(
		RecentType type,
		Long id,
		Pageable pageable
	) {
		List<RecentResponse> recentResponses = queryFactory.select(
			Projections.fields(RecentResponse.class, recent.id, recent.keyword, recent.content, recent.type,
				recent.category, recent.restaurantId))
			.from(recent)
			.where(eqType(type), eqUserId(id))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(recent.id.desc())
			.fetch();

		Long totalCount = queryFactory.select(Wildcard.count)
			.from(recent)
			.where(eqType(type), eqUserId(id))
			.fetch()
			.get(0);

		return new PageWrapper<>(recentResponses, pageable.getPageNumber(), pageable.getPageSize(), totalCount);
	}

	private BooleanExpression eqType(RecentType type) {
		return recent.type.eq(type);
	}

	private BooleanExpression eqUserId(Long id) {
		return id != null ? recent.user.id.eq(id) : null;
	}
}
