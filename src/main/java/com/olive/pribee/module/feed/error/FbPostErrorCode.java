package com.olive.pribee.module.feed.error;

import org.springframework.http.HttpStatus;

import com.olive.pribee.global.error.ErrorCode;

import lombok.Getter;

@Getter
public enum FbPostErrorCode implements ErrorCode {
	INVALID_QUIZ_ID(HttpStatus.NOT_FOUND, "권한이 없거나 존재하지 않는 게시물입니다.");

	private final HttpStatus httpStatus;
	private final String message;

	FbPostErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
