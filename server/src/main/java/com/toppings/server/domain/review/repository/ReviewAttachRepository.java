package com.toppings.server.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.toppings.server.domain.review.entity.ReviewAttach;

public interface ReviewAttachRepository extends JpaRepository<ReviewAttach, Long> {
}
