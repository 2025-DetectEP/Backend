package com.olive.pribee.global.enums;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum DetectLikelihood {
	VERY_UNLIKELY("VERY_UNLIKELY", "가능성 매우 낮음"),
	UNLIKELY("UNLIKELY", "가능성 낮음"),
	POSSIBLE("POSSIBLE", "가능성 있음"),
	LIKELY("LIKELY", "가능성 높음"),
	VERY_LIKELY("VERY_LIKELY", "가능성 매우 높음");

	private final String code;
	private final String name;

	private DetectLikelihood(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static DetectLikelihood of(String code) {
		return Arrays.stream(DetectLikelihood.values())
			.filter(r -> r.getCode().equals(code))
			.findAny()
			.orElse(null);
	}
}
