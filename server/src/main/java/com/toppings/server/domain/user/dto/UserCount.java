package com.toppings.server.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCount {

	private int postCount;

	private int scrapCount;

	private int reviewCount;

	public static UserCount of(int postCount, int scrapCount, int reviewCount) {
		return UserCount.builder()
			.postCount(postCount)
			.scrapCount(scrapCount)
			.reviewCount(reviewCount)
			.build();
	}
}
