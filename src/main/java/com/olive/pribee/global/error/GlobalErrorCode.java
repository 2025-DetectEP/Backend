package com.olive.pribee.global.error;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum GlobalErrorCode implements ErrorCode {
	INVALID_FACEBOOK_CODE(HttpStatus.UNAUTHORIZED, "facebook code가 유효하지 않습니다."),
	AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다."),
	AUTHORIZATION_FAILED(HttpStatus.UNAUTHORIZED, "인가에 실패하였습니다."),
	ACCESS_TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "Access Token이 필요합니다."),
	REFRESH_TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "Refresh Token이 필요합니다."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Token이 유효하지 않습니다."),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 사용자를 찾을 수 없습니다."),
	LOGIN_REQUIRED(HttpStatus.FORBIDDEN, "로그인을 해주세요."),
	EXPIRED_JWT(HttpStatus.FORBIDDEN, "Token이 만료되었습니다."),
	MISSING_HEADER(HttpStatus.BAD_REQUEST, "필수 요청 헤더가 누락되었습니다."),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "요청 경로가 지원되지 않습니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	GlobalErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
