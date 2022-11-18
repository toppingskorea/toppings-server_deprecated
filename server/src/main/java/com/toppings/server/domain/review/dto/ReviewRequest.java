package com.toppings.server.domain.review.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.toppings.server.domain.recent.dto.RecentRequest;
import com.toppings.server.domain.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewRequest {

	@NotBlank(message = "내용을 확인해주세여.")
	private String description;

	@NotNull(message = "이미지를 확인해주세요")
	@Size(min = 1, message = "이미지를 확인해주세요")
	private List<String> images;

	public static Review dtoToEntity(ReviewRequest request) {
		return Review.builder()
			.description(request.getDescription())
			.build();
	}
}
