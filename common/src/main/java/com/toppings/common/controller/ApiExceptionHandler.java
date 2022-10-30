package com.toppings.common.controller;

import java.util.Objects;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.toppings.common.constants.ResponseCode;
import com.toppings.common.dto.ApiErrorResponse;
import com.toppings.common.exception.GeneralException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

	@ExceptionHandler
	public ResponseEntity<Object> general(
		GeneralException ex,
		WebRequest request
	) {
		ResponseCode responseCode = ex.getResponseCode();
		HttpStatus status = responseCode.isClientSideError()
			? HttpStatus.BAD_REQUEST
			: HttpStatus.INTERNAL_SERVER_ERROR;
		return handleExceptionInternal(ex, responseCode, HttpHeaders.EMPTY, status, request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> exception(
		Exception ex,
		WebRequest request
	) {
		return handleExceptionInternal(
			ex, ResponseCode.INTERNAL_ERROR, HttpHeaders.EMPTY, HttpStatus.INTERNAL_SERVER_ERROR, request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> accessDeniedException(
		AccessDeniedException ex,
		WebRequest request
	) {
		return handleExceptionInternal(
			ex, ResponseCode.ACCESS_DENIED, HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler
	public ResponseEntity<Object> validation(
		ConstraintViolationException ex,
		WebRequest request
	) {
		return handleExceptionInternal(
			ex, ResponseCode.VALIDATION_ERROR, HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(
		MissingServletRequestPartException ex,
		HttpHeaders headers,
		HttpStatus status,
		WebRequest request
	) {
		return handleExceptionInternal(
			ex, ResponseCode.FILE_NOT_FOUND, HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex,
		HttpHeaders headers,
		HttpStatus status,
		WebRequest request
	) {
		String message = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
		return handleExceptionInternal(ex, request, message);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(
		BindException ex,
		HttpHeaders headers,
		HttpStatus status,
		WebRequest request
	) {
		BindingResult bindingResult = ex.getBindingResult();
		String message = null;
		if (Objects.equals(Objects.requireNonNull(bindingResult.getFieldError()).getCode(),
			TypeMismatchException.ERROR_CODE))
			message = ResponseCode.BAD_REQUEST.getMessage();
		else
			message = bindingResult.getFieldErrors().get(0).getDefaultMessage();
		return handleExceptionInternal(ex, request, message);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
		Exception ex,
		Object body,
		HttpHeaders headers,
		HttpStatus status,
		WebRequest request
	) {
		ResponseCode responseCode = status.is4xxClientError()
			? ResponseCode.BAD_REQUEST
			: ResponseCode.INTERNAL_ERROR;
		return handleExceptionInternal(ex, responseCode, headers, status, request);
	}

	private ResponseEntity<Object> handleExceptionInternal(
		Exception ex,
		ResponseCode responseCode,
		HttpHeaders headers,
		HttpStatus status,
		WebRequest request
	) {
		logger.error("======================================================================");
		logger.error("Exception : {}, {}", responseCode.getCode(), responseCode.getMessage());
		logger.error("======================================================================");
		ex.printStackTrace();

		return super.handleExceptionInternal(ex,
			ApiErrorResponse.of(false, responseCode.getCode(), responseCode.getMessage()), headers, status, request
		);
	}

	private ResponseEntity<Object> handleExceptionInternal(
		Exception ex,
		WebRequest request,
		String message
	) {
		logger.error("======================================================================");
		logger.error("Validation Exception: {} - {}", ex.getMessage(), message);
		logger.error("======================================================================");
		ex.printStackTrace();

		return super.handleExceptionInternal(
			ex, ApiErrorResponse.of(false, ResponseCode.VALIDATION_ERROR.getCode(),
				ResponseCode.VALIDATION_ERROR.getMessage(message)
			), HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST, request);
	}
}
