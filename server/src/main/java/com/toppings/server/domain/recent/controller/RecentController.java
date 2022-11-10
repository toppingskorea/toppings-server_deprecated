package com.toppings.server.domain.recent.controller;

import com.toppings.common.dto.ApiDataResponse;
import com.toppings.server.domain.recent.constant.RecentType;
import com.toppings.server.domain.recent.dto.RecentRequest;
import com.toppings.server.domain.recent.service.RecentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recent")
public class RecentController {

    private final RecentService recentService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> registerRecent(
        @RequestBody RecentRequest recentRequest,
        @AuthenticationPrincipal Long id
    ) {
        return ResponseEntity.ok(ApiDataResponse.of(recentService.register(recentRequest, id)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getRecents(
        RecentType type,
        @AuthenticationPrincipal Long id
    ) {
        return ResponseEntity.ok(ApiDataResponse.of(recentService.getRecents(type, id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> removeOneRecent( @PathVariable Long id) {
        return ResponseEntity.ok(ApiDataResponse.of(recentService.removeOneRecent(id)));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> removeAllRecent(@AuthenticationPrincipal Long id) {
        return ResponseEntity.ok(ApiDataResponse.of(recentService.removeAllRecent(id)));
    }
}
