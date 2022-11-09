package com.toppings.server.domain.restaurant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toppings.common.dto.ApiDataResponse;
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
	public ResponseEntity<?> registerRestaurant() {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

	/**
	 * 음식점 목록 조회하기
	 */
	@GetMapping
	public ResponseEntity<?> getRestaurants() {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}


	/**
	 * 음식점 상세 조회하기
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getRestaurant(@PathVariable Long id) {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

}
