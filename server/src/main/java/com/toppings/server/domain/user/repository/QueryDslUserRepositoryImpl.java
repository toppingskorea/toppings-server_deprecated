// package com.toppings.server.domain.user.repository;
//
//
// import java.util.List;
//
// import org.springframework.stereotype.Repository;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.querydsl.core.types.Projections;
// import com.querydsl.core.types.dsl.BooleanExpression;
// import com.querydsl.jpa.impl.JPAQueryFactory;
// import com.toppings.server.domain_global.constants.Auth;
// import com.toppings.server.domain.user.dto.AdminResponse;
//
// import lombok.RequiredArgsConstructor;
//
// @Repository
// @RequiredArgsConstructor
// @Transactional(readOnly = true)
// public class QueryDslUserRepositoryImpl implements QueryDslUserRepository {
//
// 	private final JPAQueryFactory queryFactory;
//
// 	@Override
// 	public List<AdminResponse> findAdminAll() {
// 		return queryFactory.select(
// 			Projections.fields(AdminResponse.class, user.userId.as("id"), user.name, user.username, user.createDate))
// 			.from(user)
// 			.where(eqDeleteYn("N"), eqRoleAdmin())
// 			.fetch();
// 	}
//
// 	private BooleanExpression eqRoleAdmin() {
// 		return user.role.eq(Auth.ROLE_ADMIN);
// 	}
//
// 	private BooleanExpression eqDeleteYn(String deleteYn) {
// 		return user.deleteYn.eq(deleteYn);
// 	}
// }
