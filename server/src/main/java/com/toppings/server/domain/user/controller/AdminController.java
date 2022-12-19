package com.toppings.server.domain.user.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<?> registerAdmin(@Valid @RequestBody AdminRegisterRequest registerRequest) {
		return ResponseEntity.ok(ApiDataResponse.of(adminService.register(registerRequest)));
	}
}
