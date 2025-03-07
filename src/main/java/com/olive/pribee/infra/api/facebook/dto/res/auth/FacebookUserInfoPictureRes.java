package com.olive.pribee.infra.api.facebook.dto.res.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookUserInfoPictureRes {

	private Data data;

	@Builder
	public FacebookUserInfoPictureRes(@JsonProperty("data") Data data) {
		this.data = data;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Data {
		private int height;
		private boolean isSilhouette;
		private String url;
		private int width;

		@Builder
		public Data(
			@JsonProperty("height") int height,
			@JsonProperty("is_silhouette") boolean isSilhouette,
			@JsonProperty("url") String url,
			@JsonProperty("width") int width
		) {
			this.height = height;
			this.isSilhouette = isSilhouette;
			this.url = url;
			this.width = width;
		}
	}
}