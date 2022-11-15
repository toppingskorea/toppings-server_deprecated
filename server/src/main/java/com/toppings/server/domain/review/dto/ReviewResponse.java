package com.toppings.server.domain.review.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.toppings.server.domain.review.entity.Review;
import com.toppings.server.domain.user.constant.Habit;
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

	@JsonSerialize(using= LocalDateTimeSerializer.class)
	@JsonDeserialize(using= LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
	private LocalDateTime modifiedAt;

	private String name;

	private String country;

	private List<Habit> habits;

	private Boolean isMine;

	public static ReviewResponse entityToDto(
		Review review,
		User user
	) {
		return ReviewResponse.builder()
			.id(review.getId())
			.description(review.getDescription())
			.modifiedAt(review.getUpdateDate())
			.images(review.getImages())
			.name(user.getName())
			.country(user.getCountry())
			.habits(user.getHabits())
			.build();
	}

	public static ReviewResponse entityToDto(Review review) {
		return ReviewResponse.builder()
			.id(review.getId())
			.description(review.getDescription())
			.modifiedAt(review.getUpdateDate())
			.images(review.getImages())
			.build();
	}
}
