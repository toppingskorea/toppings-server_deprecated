package com.toppings.server.domain.recent.repository;

import com.toppings.server.domain.recent.entity.Recent;
import com.toppings.server.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecentRepository extends JpaRepository<Recent, Long>, QueryDslRecentRepository {

    Optional<Recent> findRecentByKeywordAndUser(String keyword, User user);

    void deleteAllByUser(User user);
}
