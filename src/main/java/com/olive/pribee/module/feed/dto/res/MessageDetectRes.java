package com.olive.pribee.module.feed.dto.res;

import com.olive.pribee.global.enums.DetectKeyword;
import com.olive.pribee.global.enums.DetectLikelihood;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Facebook 게시물 상세 메세지 분석 응답 DTO")
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class MessageDetectRes {

	@Schema(description = "유출 위험 글자")
	private String detectWord;

	@Schema(description = "탐지된 항목", example = "PERSON_NAME")
	private DetectKeyword keyword;

	@Schema(description = "신뢰도", example = "POSSIBLE")
	private DetectLikelihood likelihood;

	@Schema(description = "유출 위험 글자 시작 지점")
	private Integer startAt;

	@Schema(description = "유출 위험 글자 끝 지점")
	private Integer endAt;

	public static MessageDetectRes of(
		String detectWord,
		DetectKeyword keyword,
		DetectLikelihood likelihood,
		Integer startAt,
		Integer endAt
	) {
		return MessageDetectRes.builder()
			.detectWord(detectWord)
			.keyword(keyword)
			.likelihood(likelihood)
			.startAt(startAt)
			.endAt(endAt)
			.build();
	}
}
