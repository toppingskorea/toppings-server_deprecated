package com.toppings.server.domain.user.repository;

import static com.toppings.server.domain.recent.entity.QRecent.*;
import static com.toppings.server.domain.user.entity.QUser.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.common.dto.PageWrapper;
import com.toppings.server.domain.user.constant.Auth;
import com.toppings.server.domain.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslUserRepositoryImpl implements QueryDslUserRepository {

	private final JPAQueryFactory queryFactory;

	@Override
	public Page<UserResponse> findAllUser(Pageable pageable) {
		List<UserResponse> recentResponses = queryFactory.select(
			Projections.fields(UserResponse.class,
				user.id,
				user.name,
				user.email,
				user.country,
				user.profile,
				user.habitContents,
				user.role,
				user.createDate
			))
			.from(user)
			.where(user.role.in(Auth.ROLE_USER, Auth.ROLE_TEMP))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.orderBy(user.id.desc())
			.fetch();

		Long totalCount = queryFactory.select(Wildcard.count)
			.from(user)
			.where(user.role.in(Auth.ROLE_USER, Auth.ROLE_TEMP))
			.fetch()
			.get(0);

		return new PageWrapper<>(recentResponses, pageable.getPageNumber(), pageable.getPageSize(), totalCount);
	}
}
