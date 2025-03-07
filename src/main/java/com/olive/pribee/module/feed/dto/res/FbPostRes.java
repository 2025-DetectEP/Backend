package com.olive.pribee.module.feed.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import com.olive.pribee.global.enums.DetectKeyword;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Facebook 게시물 리스트 응답 DTO")
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class FbPostRes {
	@Schema(description = "게시물 ID", example = "123")
	private Long id;

	@Schema(description = "게시물 생성 시간", example = "2024-03-07T12:34:56")
	private LocalDateTime createdTime;

	@Schema(description = "게시물의 첫 번째 사진 URL", example = "https://example.com/photo1.jpg")
	private String firstPhotoUrl;

	@Schema(description = "감지된 키워드 목록", example = "[\"EMAIL_ADDRESS\", \"PHONE_NUMBER\"]")
	private List<DetectKeyword> detectedKeywords;

	public static FbPostRes of(
		Long id,
		LocalDateTime createdTime,
		String firstPhotoUrl,
		List<DetectKeyword> detectedKeywords
	) {
		return FbPostRes.builder()
			.id(id)
			.createdTime(createdTime)
			.firstPhotoUrl(firstPhotoUrl)
			.detectedKeywords(detectedKeywords)
			.build();
	}
}
