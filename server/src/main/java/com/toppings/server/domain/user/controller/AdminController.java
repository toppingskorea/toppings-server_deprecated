package com.toppings.server.domain.user.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toppings.common.dto.ApiDataResponse;
import com.toppings.server.domain.user.dto.AdminRegisterRequest;
import com.toppings.server.domain.user.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

	private final AdminService adminService;

	/**
	 * 관리자 등록
	 */
	@PostMapping
	// TODO: 권한 처리 추가
	public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminRegisterRequest registerRequest) {
		return ResponseEntity.ok(ApiDataResponse.of(adminService.register(registerRequest)));
	}

	@GetMapping("/count")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getTotalCount() {
		return ResponseEntity.ok(ApiDataResponse.of(adminService.getTotalCount()));
	}
}
