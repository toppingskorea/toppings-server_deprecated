package com.toppings.server.domain.likes.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LikesPercent {

	private String country;

	private String habit;

	private Long count;

	private int percent;

	public void calculatePercent(Long totalCount) {
		double divisionValue = this.count / (double)totalCount;
		this.percent = Math.toIntExact(Math.round(divisionValue * 100));
	}
}
