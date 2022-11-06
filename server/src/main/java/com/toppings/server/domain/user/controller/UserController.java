package com.toppings.server.domain.user.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toppings.common.dto.ApiDataResponse;
import com.toppings.server.domain.user.dto.UserModifyRequest;
import com.toppings.server.domain.user.dto.UserRegisterRequest;
import com.toppings.server.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	/**
	 * 회원가입
	 */
	@PostMapping
	public ResponseEntity<?> registerUser(
		@Valid @RequestBody UserRegisterRequest userRegisterRequest,
		@AuthenticationPrincipal Long id
	) {
		return ResponseEntity.ok(ApiDataResponse.of(userService.registerUser(userRegisterRequest, id)));
	}

	/**
	 * 유저 목록 조회 (admin)
	 */
	@GetMapping("/admin")
	public ResponseEntity<?> getUsersForAdmin() {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

	/**
	 * 유저 회원가입 검증
	 */
	@GetMapping("/reg-check")
	public ResponseEntity<?> verifyRegister(@AuthenticationPrincipal Long id) {
		return ResponseEntity.ok(ApiDataResponse.of(userService.verifyRegister(id)));
	}

	/**
	 * 유저 전체 정보 조회
	 */
	@GetMapping
	public ResponseEntity<?> getUser(@AuthenticationPrincipal Long id) {
		return ResponseEntity.ok(ApiDataResponse.of(""));
	}

	/**
	 * 유저 프로필 수정
	 */
	@PutMapping
	public ResponseEntity<?> modifyUser(
		@RequestBody UserModifyRequest userRegisterRequest,
		@AuthenticationPrincipal Long id
	) {
		return ResponseEntity.ok(ApiDataResponse.of(userService.modifyUser(userRegisterRequest, id)));
	}

	/**
	 * 유저 스크랩 조회
	 */

	/**
	 * 유저 게시글 조회
	 */

	/**
	 * 유저 리뷰 조회
	 */
}
