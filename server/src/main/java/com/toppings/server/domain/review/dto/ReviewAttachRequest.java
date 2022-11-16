package com.toppings.server.domain.review.dto;

import com.toppings.server.domain.review.entity.Review;
import com.toppings.server.domain.review.entity.ReviewAttach;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewAttachRequest {

	private String name;

	public static ReviewAttach dtoToEntity(ReviewAttachRequest request, Review review) {
		return ReviewAttach.builder()
			.image(request.getName())
			.review(review)
			.build();
	}
}
