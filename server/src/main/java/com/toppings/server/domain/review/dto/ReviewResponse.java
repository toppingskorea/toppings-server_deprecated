package com.toppings.server.domain.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toppings.server.domain.review.entity.Review;
import com.toppings.server.domain.user.dto.UserResponse;
import com.toppings.server.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewResponse {

	private Long id;

	private String description;

	private List<String> images;

	private LocalDateTime modifiedAt;

	private UserResponse user;

	public static ReviewResponse entityToDto(Review review) {
		return ReviewResponse.builder()
			.id(review.getId())
			.description(review.getDescription())
			.modifiedAt(review.getUpdateDate())
			.images(review.getImages())
			.build();
	}
}
