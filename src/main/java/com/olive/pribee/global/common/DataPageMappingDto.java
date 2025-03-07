package com.olive.pribee.global.common;

import lombok.Getter;

@Getter
public class DataPageMappingDto<T> {
	private final T data;
	private final long totalElements;
	private final int totalPages;
	private final int size;
	private final int numberOfElements;

	private DataPageMappingDto(T data, long totalElements, int totalPages, int size,
		int numberOfElements) {
		this.data = data;
		this.totalElements = totalElements;
		this.size = size;
		this.numberOfElements = numberOfElements;
		this.totalPages = totalPages;
	}

	private DataPageMappingDto(T data, String message, long totalElements, int totalPages, int size,
		int numberOfElements) {
		this.data = data;
		this.totalElements = totalElements;
		this.size = size;
		this.numberOfElements = numberOfElements;
		this.totalPages = totalPages;
	}

	public static <T> DataPageMappingDto<T> of(T data, long totalElements, int totalPages, int size,
		int numberOfElements) {
		return new DataPageMappingDto<>(data, totalElements, totalPages, size, numberOfElements);
	}

	public static <T> DataPageMappingDto<T> of(T data, Integer code, String message, long totalElements,
		int totalPages, int size,
		int numberOfElements) {
		return new DataPageMappingDto<>(data, message, totalElements, totalPages, size, numberOfElements);
	}
}