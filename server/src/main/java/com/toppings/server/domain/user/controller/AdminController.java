package com.toppings.server.domain.user.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toppings.common.dto.ApiDataResponse;
import com.toppings.common.dto.PageResultResponse;
import com.toppings.server.domain.user.dto.AdminRegisterRequest;
import com.toppings.server.domain.user.service.AdminService;
import com.toppings.server.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

	private final AdminService adminService;

	private final UserService userService;

	/**
	 * 관리자 등록
	 */
	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminRegisterRequest registerRequest) {
		return ResponseEntity.ok(ApiDataResponse.of(adminService.register(registerRequest)));
	}

	/**
	 * 관리자 메인페이지 카운트 조회
	 */
	@GetMapping("/count")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getTotalCount() {
		return ResponseEntity.ok(ApiDataResponse.of(adminService.getTotalCount()));
	}

	/**
	 * 유저 목록 조회 (admin)
	 */
	@GetMapping("/users")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getUsersForAdmin(@PageableDefault Pageable pageable) {
		return ResponseEntity.ok(ApiDataResponse.of(PageResultResponse.of(userService.getUsers(pageable))));
	}

}
