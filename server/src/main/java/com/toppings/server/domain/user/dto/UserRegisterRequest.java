package com.toppings.server.domain.user.dto;

import javax.validation.constraints.NotBlank;

import com.toppings.server.domain.user.constant.Habit;

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

	private Habit habit;
}
