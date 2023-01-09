package com.toppings.server.domain.user.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserModifyRequest {

	@NotBlank(message = "이름을 확인해주세요.")
	private String name;

	@NotBlank(message = "국적을 확인해주세요.")
	private String country;

	private List<UserHabitRequest> habits;

	private String profile;

	public boolean notEmptyHabit() {
		return this.habits != null && !this.habits.isEmpty();
	}
}
