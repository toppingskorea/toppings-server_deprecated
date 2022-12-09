package com.toppings.server.domain.user.dto;

import javax.validation.constraints.NotBlank;

import com.toppings.server.domain.user.constant.Auth;
import com.toppings.server.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRegisterRequest {

	@NotBlank(message = "아이디를 확인해주세요.")
	private String username;

	@NotBlank(message = "비밀번호를 확인해주세요.")
	private String password;

	public static User dtoToEntity(AdminRegisterRequest registerRequest) {
		return User.builder()
			.username(registerRequest.getUsername())
			.role(Auth.ROLE_ADMIN)
			.build();
	}
}
