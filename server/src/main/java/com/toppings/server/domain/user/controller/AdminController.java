package com.toppings.server.domain.user.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/admin")
public class AdminController {

	private final AdminService adminService;

	/**
	 * 관리자 계정 등록
	 * @param adminRegisterRequest
	 * @param principalId
	 * @return ResponseEntity<Boolean>
	 */
	@PostMapping
	@PreAuthorize("hasRole('ROLE_HEAD')")
	public ResponseEntity<?> insertAdmin(
		@Valid @RequestBody AdminRegisterRequest adminRegisterRequest,
		@AuthenticationPrincipal Long principalId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(adminService.insertAdmin(adminRegisterRequest, principalId)));
	}

	/**
	 * 관리자 목록 조회
	 * @return ResponseEntity<List<AdminResponse>>
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_HEAD')")
	public ResponseEntity<?> getAdminAll() {
		return ResponseEntity.ok(ApiDataResponse.of(adminService.getAdminAll()));
	}

	/**
	 * 관리자 삭제
	 * @param delId
	 * @param principalId
	 * @return ResponseEntity<Boolean>
	 */
	@DeleteMapping("/{delId}")
	@PreAuthorize("hasRole('ROLE_HEAD')")
	public ResponseEntity<?> deleteAdmin(
		@PathVariable Long delId,
		@AuthenticationPrincipal Long principalId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(adminService.deleteAdmin(delId, principalId)));
	}
}
