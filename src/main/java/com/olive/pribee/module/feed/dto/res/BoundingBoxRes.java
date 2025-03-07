
package com.olive.pribee.module.feed.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Facebook 게시물 상세 사진 탐지 위치 응답 DTO")
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class BoundingBoxRes {

	@Schema(description = "x 좌표")
	private int x;

	@Schema(name = "y 좌표")
	private int y;

	@Schema(description = "너비")
	private int width;

	@Schema(description = "높이")
	private int height;

	public static BoundingBoxRes of(
		int x,
		int y,
		int width,
		int height
	) {
		return BoundingBoxRes.builder()
			.x(x)
			.y(y)
			.width(width)
			.height(height)
			.build();
	}
}
