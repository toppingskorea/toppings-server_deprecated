package com.toppings.common.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PubRequest {

	@NotBlank(message = "공개 여부를 확인해주세요.")
	private String isPub;
}
