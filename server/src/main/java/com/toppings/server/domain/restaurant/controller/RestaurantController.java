package com.toppings.server.domain.restaurant.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toppings.common.dto.ApiDataResponse;
import com.toppings.server.domain.restaurant.dto.RestaurantModifyRequest;
import com.toppings.server.domain.restaurant.dto.RestaurantRequest;
import com.toppings.server.domain.restaurant.service.RestaurantService;
import com.toppings.server.domain.review.dto.ReviewModifyRequest;
import com.toppings.server.domain.review.dto.ReviewRequest;
import com.toppings.server.domain.review.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {

	private final RestaurantService restaurantService;

	/**
	 * 음식점 등록하기
	 */
	@PostMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> registerRestaurant(
		@Valid @RequestBody RestaurantRequest restaurantRequest,
		@AuthenticationPrincipal Long id
	) {
		return ResponseEntity.ok(ApiDataResponse.of(restaurantService.register(restaurantRequest, id)));
	}

	/**
	 * 음식점 수정하기
	 */
	@PutMapping("/{restaurantId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> modifyRestaurant(
		@RequestBody RestaurantModifyRequest restaurantModifyRequest,
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(
			ApiDataResponse.of(restaurantService.modify(restaurantModifyRequest, restaurantId, userId)));
	}

	/**
	 * 음식점 목록 조회하기 (필터링)
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getRestaurants() {
		return ResponseEntity.ok(ApiDataResponse.of(restaurantService.findAll()));
	}

	/**
	 * 음식점 상세 조회하기
	 */
	@GetMapping("/{restaurantId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getRestaurant(@PathVariable Long restaurantId) {
		return ResponseEntity.ok(ApiDataResponse.of(restaurantService.findOne(restaurantId)));
	}

	@DeleteMapping("/{restaurantId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> removeRestaurant(
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(restaurantService.remove(restaurantId, userId)));
	}

	// ---- review
	private final ReviewService reviewService;

	@PostMapping("/{restaurantId}/review")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> registerReview(
		@Valid @RequestBody ReviewRequest request,
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(reviewService.register(request, restaurantId, userId)));
	}

	@GetMapping("/{restaurantId}/review")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getReviews(
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(reviewService.findAll(restaurantId, userId)));
	}
}
