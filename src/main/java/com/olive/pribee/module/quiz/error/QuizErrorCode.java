package com.olive.pribee.module.quiz.error;

import org.springframework.http.HttpStatus;

import com.olive.pribee.global.error.ErrorCode;

import lombok.Getter;

@Getter
public enum QuizErrorCode implements ErrorCode {
	INVALID_QUIZ_ID(HttpStatus.NOT_FOUND, "해당하는 퀴즈가 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;

	QuizErrorCode(HttpStatus httpStatus, String message) {
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
