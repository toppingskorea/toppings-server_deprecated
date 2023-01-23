package com.toppings.server.domain.review.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toppings.common.dto.ApiDataResponse;
import com.toppings.common.dto.PageResultResponse;
import com.toppings.common.dto.PubRequest;
import com.toppings.server.domain.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/review")
public class AdminReviewController {

	private final ReviewService reviewService;

	/**
	 * 리뷰 상세 조회하기 (관리자)
	 */
	@GetMapping("/{reviewId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getRestaurantForAdmin(@PathVariable Long reviewId) {
		return ResponseEntity.ok(ApiDataResponse.of(reviewService.findOneForAdmin(reviewId)));
	}

	/**
	 * 리뷰 목록 조회하기 (관리자)
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getRestaurantsForAdmin(@PageableDefault Pageable pageable) {
		return ResponseEntity.ok(
			ApiDataResponse.of(PageResultResponse.of(reviewService.findAllForAdmin(pageable))));
	}

	/**
	 * 리뷰 공개여부 수정하기
	 */
	@PutMapping("/{reviewId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> modifyRestaurantPub(
		@Valid @RequestBody PubRequest pubRequest,
		@PathVariable Long reviewId
	) {
		return ResponseEntity.ok(
			ApiDataResponse.of(reviewService.modifyPub(pubRequest, reviewId)));
	}
}
