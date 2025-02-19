package com.olive.pribee.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum GlobalErrorCode implements ErrorCode {
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "요청 경로가 지원되지 않습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	GlobalErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
