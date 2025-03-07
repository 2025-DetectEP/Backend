package com.olive.pribee.module.feed.dto.res;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Facebook 게시물 상세 응답 DTO")
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class FbPostDetailRes {
	@Schema(description = "게시물 생성 시간", example = "2024-03-07T12:34:56")
	private LocalDateTime createdTime;

	@Schema(description = "chat gpt 에서 제안한 수정본")
	private String safeMessage;

	@Schema(description = "원본 메세지")
	private String message;

	@Schema(description = "페이지 이동 링크")
	private String permalinkUrl;

	@Schema(description = "메세지 분석 정보")
	private List<MessageDetectRes> messageDetectRes;

	@Schema(description = "사진 분석 정보")
	private List<PhotoUrlRes> photoUrlRes;

	public static FbPostDetailRes of(
		LocalDateTime createdTime,
		String message,
		String permalinkUrl,
		String safeMessage,
		List<MessageDetectRes> messageDetectRes,
		List<PhotoUrlRes> photoUrlRes
	) {
		return FbPostDetailRes.builder()
			.message(message)
			.permalinkUrl(permalinkUrl)
			.createdTime(createdTime)
			.safeMessage(safeMessage)
			.messageDetectRes(messageDetectRes)
			.photoUrlRes(photoUrlRes)
			.build();
	}
}
