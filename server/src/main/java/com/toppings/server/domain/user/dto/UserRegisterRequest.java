package com.toppings.server.domain.user.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRegisterRequest {

	@NotBlank(message = "국적을 입력해주세요.")
	private String country;

	@NotBlank(message = "식습관을 입력해주세요.")
	private String habit;
}
