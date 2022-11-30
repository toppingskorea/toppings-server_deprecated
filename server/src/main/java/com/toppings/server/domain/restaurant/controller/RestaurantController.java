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
import com.toppings.server.domain.likes.service.LikeService;
import com.toppings.server.domain.restaurant.dto.RestaurantModifyRequest;
import com.toppings.server.domain.restaurant.dto.RestaurantRequest;
import com.toppings.server.domain.restaurant.dto.RestaurantSearchRequest;
import com.toppings.server.domain.restaurant.service.RestaurantService;
import com.toppings.server.domain.review.dto.ReviewRequest;
import com.toppings.server.domain.review.service.ReviewService;
import com.toppings.server.domain.scrap.service.ScrapService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {

	private final RestaurantService restaurantService;

	private final ReviewService reviewService;

	private final LikeService likeService;

	private final ScrapService scrapService;

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
	public ResponseEntity<?> getRestaurants(
		@Valid RestaurantSearchRequest restaurantSearchRequest,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(restaurantService.findAll(restaurantSearchRequest, userId)));
	}

	/**
	 * 음식점 상세 조회하기
	 */
	@GetMapping("/{restaurantId}")
	public ResponseEntity<?> getRestaurant(
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(restaurantService.findOne(restaurantId, userId)));
	}

	/**
	 * 음식점 상세 조회하기
	 */
	@GetMapping("/{restaurantId}/like")
	public ResponseEntity<?> getRestaurantLikePercent(@PathVariable Long restaurantId) {
		return ResponseEntity.ok(ApiDataResponse.of(restaurantService.getLikesPercent(restaurantId)));
	}


	/**
	 * 음식점 삭제하기
	 */
	@DeleteMapping("/{restaurantId}")
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public ResponseEntity<?> removeRestaurant(
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(restaurantService.remove(restaurantId, userId)));
	}

	// ---- review

	/**
	 * 음식점 리뷰 등록하기
	 */
	@PostMapping("/{restaurantId}/review")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> registerReview(
		@Valid @RequestBody ReviewRequest request,
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(reviewService.register(request, restaurantId, userId)));
	}

	/**
	 * 음식점 리뷰 목록 조회하기
	 */
	@GetMapping("/{restaurantId}/review")
	public ResponseEntity<?> getReviews(
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(reviewService.findAll(restaurantId, userId)));
	}

	// ---- like

	/**
	 * 음식점 좋아요
	 */
	@PostMapping("/{restaurantId}/like")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> doLike(
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(likeService.register(restaurantId, userId)));
	}

	/**
	 * 음식점 좋아요 취소
	 */
	@DeleteMapping("/{restaurantId}/like")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> doUnLike(
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(likeService.remove(restaurantId, userId)));
	}

	// ---- scrap

	/**
	 * 음식점 스크랩
	 */
	@PostMapping("/{restaurantId}/scrap")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> doScrap(
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(scrapService.register(restaurantId, userId)));
	}

	/**
	 * 음식점 스크랩 취소
	 */
	@DeleteMapping("/{restaurantId}/scrap")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> doUnScrap(
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(scrapService.remove(restaurantId, userId)));
	}
}
