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
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> modifyRestaurant(
		@RequestBody RestaurantModifyRequest restaurantModifyRequest,
		@PathVariable Long id,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(restaurantService.modify(restaurantModifyRequest, id, userId)));
	}

	/**
	 * 음식점 목록 조회하기 (필터링)
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getRestaurants() {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

	/**
	 * 음식점 상세 조회하기
	 */
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getRestaurant(@PathVariable Long id) {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> removeRestaurant(
		@PathVariable Long id,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(restaurantService.remove(id, userId)));
	}
}
