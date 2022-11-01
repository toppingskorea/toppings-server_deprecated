package com.toppings.common.dto;



import com.toppings.common.constants.ResponseCode;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public class ApiDataResponse<T> extends ApiErrorResponse {

	private final T data;

	public ApiDataResponse(T data) {
		super(true, ResponseCode.OK.getCode(), ResponseCode.OK.getMessage());
		this.data = data;
	}

	public static <T> ApiDataResponse<T> of(T data) {
		return new ApiDataResponse<>(data);
	}

	public static <T> ApiDataResponse<T> empty() {
		return new ApiDataResponse<>(null);
	}
}
