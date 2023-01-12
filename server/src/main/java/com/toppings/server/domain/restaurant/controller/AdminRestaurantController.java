package com.toppings.server.domain.restaurant.controller;

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
import com.toppings.server.domain.restaurant.service.RestaurantService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/restaurant")
public class AdminRestaurantController {

	private final RestaurantService restaurantService;

	/**
	 * 음식점 상세 조회하기 (관리자)
	 */
	@GetMapping("/{restaurantId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getRestaurantForAdmin(@PathVariable Long restaurantId) {
		return ResponseEntity.ok(ApiDataResponse.of(restaurantService.findOneForAdmin(restaurantId)));
	}

	/**
	 * 음식점 목록 조회하기 (관리자)
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getRestaurantsForAdmin(@PageableDefault Pageable pageable) {
		return ResponseEntity.ok(
			ApiDataResponse.of(PageResultResponse.of(restaurantService.findAllForAdmin(pageable))));
	}

	/**
	 * 음식점 공개여부 수정하기
	 */
	@PutMapping("/{restaurantId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> modifyRestaurantPub(
		@Valid @RequestBody PubRequest pubRequest,
		@PathVariable Long restaurantId
	) {
		return ResponseEntity.ok(
			ApiDataResponse.of(restaurantService.modifyPub(pubRequest, restaurantId)));
	}
}
