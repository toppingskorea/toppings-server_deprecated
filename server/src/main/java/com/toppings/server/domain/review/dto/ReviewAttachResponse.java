package com.toppings.server.domain.review.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toppings.server.domain.review.entity.ReviewAttach;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewAttachResponse {

	private String name;

	public static ReviewAttachResponse entityToDto(ReviewAttach reviewAttach) {
		return ReviewAttachResponse.builder()
			.name(reviewAttach.getImage())
			.build();
	}
}
