package com.toppings.server.domain.restaurant.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantMapSearchRequest {

	@NotNull(message = "x1 좌표를 확인해주세요.")
	private Double x1;

	@NotNull(message = "x2 좌표를 확인해주세요.")
	private Double x2;

	@NotNull(message = "y1 좌표를 확인해주세요.")
	private Double y1;

	@NotNull(message = "y2 좌표를 확인해주세요.")
	private Double y2;
}
