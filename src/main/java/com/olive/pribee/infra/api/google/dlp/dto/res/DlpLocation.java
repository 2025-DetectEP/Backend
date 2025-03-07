package com.olive.pribee.infra.api.google.dlp.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DlpLocation {
	@JsonProperty("byteRange")
	private Range byteRange;

	@JsonProperty("codepointRange")
	private Range codepointRange;

	@JsonProperty("contentLocations")  // 이미지 분석을 위한 추가 필드
	private List<ContentLocation> contentLocations;

	@Builder
	public DlpLocation(Range byteRange, Range codepointRange, List<ContentLocation> contentLocations) {
		this.byteRange = byteRange;
		this.codepointRange = codepointRange;
		this.contentLocations = contentLocations;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Range {
		@JsonProperty("start")
		private int start;

		@JsonProperty("end")
		private int end;

		@Builder
		public Range(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ContentLocation {
		@JsonProperty("imageLocation")
		private ImageLocation imageLocation;

		@Builder
		public ContentLocation(ImageLocation imageLocation) {
			this.imageLocation = imageLocation;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ImageLocation {
		@JsonProperty("boundingBoxes")
		private List<BoundingBox> boundingBoxes;

		@Builder
		public ImageLocation(List<BoundingBox> boundingBoxes) {
			this.boundingBoxes = boundingBoxes;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class BoundingBox {
		@JsonProperty("top")
		private int top;

		@JsonProperty("left")
		private int left;

		@JsonProperty("width")
		private int width;

		@JsonProperty("height")
		private int height;

		@Builder
		public BoundingBox(int top, int left, int width, int height) {
			this.top = top;
			this.left = left;
			this.width = width;
			this.height = height;
		}
	}
}
