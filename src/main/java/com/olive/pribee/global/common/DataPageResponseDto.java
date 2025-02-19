package com.olive.pribee.global.common;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class DataPageResponseDto<T> extends ResponseDto {
	private final T data;
	private final long totalElements;
	private final int totalPages;
	private final int size;
	private final int numberOfElements;

	private DataPageResponseDto(T data, Integer code, long totalElements, int totalPages, int size,
		int numberOfElements) {
		super(code, HttpStatus.valueOf(code).getReasonPhrase());
		this.data = data;
		this.totalElements = totalElements;
		this.size = size;
		this.numberOfElements = numberOfElements;
		this.totalPages = totalPages;
	}

	private DataPageResponseDto(T data, Integer code, String message, long totalElements, int totalPages, int size,
		int numberOfElements) {
		super(code, message);
		this.data = data;
		this.totalElements = totalElements;
		this.size = size;
		this.numberOfElements = numberOfElements;
		this.totalPages = totalPages;
	}

	public static <T> DataPageResponseDto<T> of(T data, Integer code, long totalElements, int totalPages, int size,
		int numberOfElements) {
		return new DataPageResponseDto<>(data, code, totalElements, totalPages, size, numberOfElements);
	}

	public static <T> DataPageResponseDto<T> of(T data, Integer code, String message, long totalElements,
		int totalPages, int size,
		int numberOfElements) {
		return new DataPageResponseDto<>(data, code, message, totalElements, totalPages, size, numberOfElements);
	}
}