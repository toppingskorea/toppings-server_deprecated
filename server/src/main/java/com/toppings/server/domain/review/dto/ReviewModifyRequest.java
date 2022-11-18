package com.toppings.server.domain.review.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.toppings.server.domain.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewModifyRequest {

	private String description;

	private List<String> images;

	public static void modifyReviewInfo(
		Review review,
		ReviewModifyRequest request,
		String image
	) {
		review.setDescription(request.getDescription() != null ? request.getDescription() : review.getDescription());
		review.setThumbnail(image);
	}
}
