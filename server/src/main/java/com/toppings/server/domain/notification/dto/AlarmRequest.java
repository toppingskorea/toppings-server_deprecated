package com.toppings.server.domain.notification.dto;

import javax.validation.constraints.NotNull;

import com.toppings.server.domain.notification.constant.AlarmType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlarmRequest {

	@NotNull(message = "음식점 또는 댓글 id 를 넣어주세요.")
	private Long id;

	@NotNull(message = "알람 유형을 확인해주세요.")
	private AlarmType type;

	private String content;
}
