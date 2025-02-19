package com.olive.pribee.global.error.exception;

import com.olive.pribee.global.error.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppException extends RuntimeException {
	private ErrorCode errorCode;
	private String message;

	public AppException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = errorCode.getMessage();
	}
}
