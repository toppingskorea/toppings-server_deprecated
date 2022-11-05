package com.toppings.server.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.toppings.server.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, QueryDslUserRepository {

	Optional<User> findUserByUsernameAndDeleteYn(
		String username,
		String deleteYn
	);

	Optional<User> findUserByUsername(String username);

	Optional<User> findUserByIdAndDeleteYn(
		Long userId,
		String deleteYn
	);

	@Modifying(clearAutomatically = true)
	@Transactional
	@Query("UPDATE User u SET u.refreshToken = :refreshToken WHERE u.id = :userId AND u.deleteYn = :delYn")
	void updateUserRefreshTokenByUserId(
		@Param("userId") Long userId,
		@Param("refreshToken") String refreshToken,
		@Param("delYn") String delYn
	);

	Optional<User> findUserByRefreshTokenAndDeleteYn(String refreshToken, String delYn);
}
