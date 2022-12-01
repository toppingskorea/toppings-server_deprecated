package com.toppings.server.domain.likes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterLikesCount {

	private Long restaurantId;

	private Long filterLikeCount;
}
