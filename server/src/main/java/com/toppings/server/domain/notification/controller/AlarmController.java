package com.toppings.server.domain.notification.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toppings.common.dto.ApiDataResponse;
import com.toppings.common.dto.PageResultResponse;
import com.toppings.server.domain.notification.dto.AlarmRequest;
import com.toppings.server.domain.notification.service.AlarmService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarm")
public class AlarmController {

	private final AlarmService alarmService;

	/**
	 * 최근 알람 조회
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> findAlarms(
		@AuthenticationPrincipal Long userId,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity.ok(ApiDataResponse.of(PageResultResponse.of(alarmService.findAlarms(userId, pageable))));
	}

	/**
	 * 알람 전송하기
	 */
	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
	public ResponseEntity<?> registerAlarm(
		@AuthenticationPrincipal Long userId,
		@Valid @RequestBody AlarmRequest alarmRequest
	) {
		return ResponseEntity.ok(ApiDataResponse.of(alarmService.registerAlarm(userId, alarmRequest)));
	}
}
