package com.toppings.server.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TotalCount {

	private Long totalUserCount;

	private Long totalPostCount;

	private Long totalReviewCount;
}
