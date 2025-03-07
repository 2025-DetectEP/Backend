package com.olive.pribee.module.feed.dto.res;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Facebook 게시물 상세 사진 url 응답 DTO")
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class PhotoUrlRes {

	@Schema(name = "사진 url")
	private String pictureUrl;
	@Schema(description = "사진 탐지 태그 리스트")
	private List<PhotoDetectRes> photoDetectRes;

	public static PhotoUrlRes of(
		String pictureUrl,
		List<PhotoDetectRes> photoDetectRes
	) {
		return PhotoUrlRes.builder()
			.pictureUrl(pictureUrl)
			.photoDetectRes(photoDetectRes)
			.build();
	}
}
