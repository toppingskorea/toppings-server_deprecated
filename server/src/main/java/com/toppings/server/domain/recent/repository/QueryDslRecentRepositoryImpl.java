package com.toppings.server.domain.recent.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.dto.RecentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.toppings.server.domain.recent.entity.QRecent.recent;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QueryDslRecentRepositoryImpl implements QueryDslRecentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<RecentResponse> getRecents(RecentType type, Long id) {
        return queryFactory.select(Projections.fields(RecentResponse.class, recent.id))
                .from(recent)
                .where(recent.type.eq(type), recent.user.id.eq(id))
                .orderBy(recent.id.desc())
                .fetch();
    }
}
