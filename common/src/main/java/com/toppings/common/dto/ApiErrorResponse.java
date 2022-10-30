package com.toppings.common.dto;



import com.toppings.common.constants.ResponseCode;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiErrorResponse {

	private final Boolean success; // is error?
	private final Integer code;
	private final String message;

	public static ApiErrorResponse of(
		Boolean success,
		Integer errorCode,
		String message
	) {
		return new ApiErrorResponse(success, errorCode, message);
	}

	public static ApiErrorResponse of(
		Boolean success,
		ResponseCode responseCode
	) {
		return new ApiErrorResponse(success, responseCode.getCode(), responseCode.getMessage());
	}

	public static ApiErrorResponse of(
		Boolean success,
		ResponseCode responseCode,
		Exception exception
	) {
		return new ApiErrorResponse(success, responseCode.getCode(), responseCode.getMessage(exception));
	}

	public static ApiErrorResponse of(
		Boolean success,
		ResponseCode responseCode,
		String message
	) {
		return new ApiErrorResponse(success, responseCode.getCode(), responseCode.getMessage(message));
	}
}
