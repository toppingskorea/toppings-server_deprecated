package com.toppings.server.domain.likes.dto;

import com.toppings.server.domain.user.constant.Habit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikesPercentGroup {

	private String country;

	private Habit habit;

	private Long count;
}
