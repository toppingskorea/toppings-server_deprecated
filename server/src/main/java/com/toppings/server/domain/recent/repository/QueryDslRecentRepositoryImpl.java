package com.toppings.server.domain.recent.repository;

import static com.toppings.server.domain.recent.entity.QRecent.*;
import static org.springframework.util.StringUtils.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.dto.RecentRequest;
import com.toppings.server.domain.recent.dto.RecentResponse;
import com.toppings.server.domain.recent.entity.Recent;
import com.toppings.server.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslRecentRepositoryImpl implements QueryDslRecentRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<RecentResponse> findRecents(
		RecentType type,
		Long id
	) {
		return queryFactory.select(
			Projections.fields(RecentResponse.class, recent.id, recent.keyword, recent.content, recent.type,
				recent.category))
			.from(recent)
			.where(eqType(type), eqUserId(id))
			.orderBy(recent.id.desc())
			.fetch();
	}

	private BooleanExpression eqType(RecentType type) {
		return recent.type.eq(type);
	}

	@Override
	public Optional<Recent> findRecentByRecentRequest(
		RecentRequest request,
		User user
	) {
		return Optional.ofNullable(queryFactory.select(recent)
			.from(recent)
			// TODO 해당 부분 전문 검색으로 바꾸고 싶다.
			.where(likeKeyword(request), eqUserId(user.getId()))
			.fetchOne());
	}

	private BooleanExpression eqUserId(Long id) {
		return id != null ? recent.user.id.eq(id) : null;
	}

	private BooleanExpression likeKeyword(RecentRequest request) {
		return request != null && hasText(request.getKeyword()) ?
			recent.keyword.contains(request.getKeyword().trim()) : null;
	}
}
