package com.toppings.server.domain.likes.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikesPercentResponse {

	private List<LikesPercent> countryPercent;

	private List<LikesPercent> habitPercent;
}
