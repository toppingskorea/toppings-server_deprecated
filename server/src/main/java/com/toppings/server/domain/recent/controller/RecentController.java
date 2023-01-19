package com.toppings.server.domain.recent.controller;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.toppings.common.dto.PageResultResponse;
import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.dto.RecentRequest;
import com.toppings.server.domain.recent.service.RecentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recent")
public class RecentController {

	private final RecentService recentService;

	/**
	 * 최근검색어 등록하기
	 */
	@PostMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> registerRecent(
		@Valid @RequestBody RecentRequest recentRequest,
		@AuthenticationPrincipal Long userId
	) {
		return ResponseEntity.ok(ApiDataResponse.of(recentService.register(recentRequest, userId)));
	}

	/**
	 * 최근검색어 목록 조회하기
	 */
	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> getRecents(
		RecentType type,
		@AuthenticationPrincipal Long userId,
		@PageableDefault Pageable pageable
	) {
		return ResponseEntity.ok(
			ApiDataResponse.of(PageResultResponse.of(recentService.findAll(type, userId, pageable))));
	}

	/**
	 * 최근검색어 삭제하기
	 */
	@DeleteMapping("/{recentId}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> removeOneRecent(@PathVariable Long recentId) {
		return ResponseEntity.ok(ApiDataResponse.of(recentService.removeOne(recentId)));
	}

	/**
	 * 최근검색어 전체 삭제하기
	 */
	@DeleteMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> removeAllRecent(@AuthenticationPrincipal Long userid) {
		return ResponseEntity.ok(ApiDataResponse.of(recentService.removeAll(userid)));
	}
}
