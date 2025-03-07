package com.olive.pribee.module.feed.dto.res;

import java.util.List;

import com.olive.pribee.global.enums.DetectKeyword;
import com.olive.pribee.global.enums.DetectLikelihood;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Facebook 게시물 상세 사진 분석 응답 DTO")
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PhotoDetectRes {

	@Schema(description = "유출 위험 글자")
	private String detectWord;

	@Schema(description = "탐지된 항목", example = "PERSON_NAME")
	private DetectKeyword keyword;

	@Schema(description = "신뢰도", example = "POSSIBLE")
	private DetectLikelihood likelihood;

	@Schema(description = "사진 탐지 영역")
	private List<BoundingBoxRes> boundingBoxRes;

	public static PhotoDetectRes of(
		String detectWord,
		DetectKeyword keyword,
		DetectLikelihood likelihood,
		List<BoundingBoxRes> boundingBoxRes
	) {
		return PhotoDetectRes.builder()
			.detectWord(detectWord)
			.keyword(keyword)
			.likelihood(likelihood)
			.boundingBoxRes(boundingBoxRes)
			.build();
	}
}
