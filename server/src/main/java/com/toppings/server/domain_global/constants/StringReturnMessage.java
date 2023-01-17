package com.toppings.server.domain_global.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StringReturnMessage {

	REGISTRATION_SUCCESS("Registration completed"),
	DELETE_SUCCESS("Delete completed"),

	;

	private final String message;
}
