package com.toppings.server.domain.notification.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.toppings.server.domain.notification.constant.AlarmType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlarmResponse {

	private Long id;

	private String userName;

	private String country;

	private String content;

	private AlarmType alarmType;

	private String restaurantName;

	private String thumbnail;

	private String message;
}
