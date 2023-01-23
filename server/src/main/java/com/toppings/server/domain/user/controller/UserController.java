package com.toppings.server.domain.user.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toppings.common.dto.ApiDataResponse;
import com.toppings.common.dto.PageResultResponse;
import com.toppings.server.domain.user.dto.UserModifyRequest;
import com.toppings.server.domain.user.dto.UserRegisterRequest;
import com.toppings.server.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

	private final UserService userService;

	/**
	 * 회원가입
	 */
	@PostMapping
	@PreAuthorize("hasRole('ROLE_TEMP')")
	public ResponseEntity<?> registerUser(
		@Valid @RequestBody UserRegisterRequest userRegisterRequest,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(userService.register(userRegisterRequest, userId)));
	}

	/**
	 * 유저 목록 조회 (admin)
	 */
	@GetMapping("/admin")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getUsersForAdmin() {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

	/**
	 * 유저 회원가입 검증
	 */
	@GetMapping("/reg-check")
	@PreAuthorize("hasRole('ROLE_TEMP') or hasRole('ROLE_USER')")
	public ResponseEntity<?> verifyRegister(@AuthenticationPrincipal Long userId) {
		return ResponseEntity.ok(ApiDataResponse.of(userService.verifyRegister(userId)));
	}

	/**
	 * 유저 정보 조회
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getUser(@AuthenticationPrincipal Long userId) {
		return ResponseEntity.ok(ApiDataResponse.of(userService.findOne(userId)));
	}

	/**
	 * 유저 프로필 수정
	 */
	@PutMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> modifyUser(
		@Valid @RequestBody UserModifyRequest userRegisterRequest,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(userService.modify(userRegisterRequest, userId)));
	}

	/**
	 * 유저 스크랩 조회
	 */
	@GetMapping("/scrap")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getScrapByUser(
		@AuthenticationPrincipal Long userId,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity.ok(
			ApiDataResponse.of(PageResultResponse.of(userService.findScrapByUser(userId, pageable))));
	}

	/**
	 * 유저 게시글 조회
	 */
	@GetMapping("/restaurant")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getRestaurantByUser(
		@AuthenticationPrincipal Long userId,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity.ok(
			ApiDataResponse.of(PageResultResponse.of(userService.findRestaurantByUser(userId, pageable))));
	}

	/**
	 * 유저 리뷰 조회
	 */
	@GetMapping("/review")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getReviewByUser(
		@AuthenticationPrincipal Long userId,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity.ok(
			ApiDataResponse.of(PageResultResponse.of(userService.findReviewByUser(userId, pageable))));
	}

	/**
	 * 유저 권한 조회
	 */
	@GetMapping("/role")
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_TEMP')")
	public ResponseEntity<?> getUserRole(@AuthenticationPrincipal Long userId) {
		return ResponseEntity.ok(ApiDataResponse.of(userService.findUserRole(userId)));
	}

	@DeleteMapping
	public ResponseEntity<?> removeUser(@AuthenticationPrincipal Long userId) {
		return ResponseEntity.ok(ApiDataResponse.of(userService.removeUser(userId)));
	}
}
