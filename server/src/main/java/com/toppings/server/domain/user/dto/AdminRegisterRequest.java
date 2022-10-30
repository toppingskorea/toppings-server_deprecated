package com.toppings.server.domain.user.dto;

import javax.validation.constraints.NotBlank;

import com.toppings.common.constants.Auth;
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

	@NotBlank(message = "아이디를 입력해주세요.")
	private String username;

	@NotBlank(message = "사용자명을 입력해주세요.")
	private String name;

	@NotBlank(message = "비밀번호를 입력해주세요.")
	private String password;

	public static User dtoToEntity(AdminRegisterRequest adminRegisterRequest) {
		return User.builder()
			.username(adminRegisterRequest.getUsername())
			.role(Auth.ROLE_ADMIN)
			.build();
	}
}
