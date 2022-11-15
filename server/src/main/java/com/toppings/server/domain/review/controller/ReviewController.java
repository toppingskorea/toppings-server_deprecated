package com.toppings.server.domain.review.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toppings.common.dto.ApiDataResponse;
import com.toppings.server.domain.review.dto.ReviewModifyRequest;
import com.toppings.server.domain.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

	private final ReviewService reviewService;

	/**
	 * 리뷰 상세 조회하기
	 */
	@GetMapping("/{reviewId}")
	public ResponseEntity<?> getReview(
		@PathVariable Long reviewId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(reviewService.findOne(reviewId, userId)));
	}

	/**
	 * 리뷰 수정하기
	 */
	@PutMapping("/{reviewId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> modifyReview(
		@RequestBody ReviewModifyRequest request,
		@AuthenticationPrincipal Long userId,
		@PathVariable Long reviewId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(reviewService.modify(request, reviewId, userId)));
	}

	/**
	 * 리뷰 삭제하기
	 */
	@DeleteMapping("/{reviewId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> removeReview(
		@PathVariable Long reviewId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(reviewService.remove(reviewId, userId)));
	}
}
