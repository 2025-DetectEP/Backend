package com.olive.pribee.infra.api.facebook.dto.res.post;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookPostRes {
	private String id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "UTC")
	private LocalDateTime updatedTime;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZ", timezone = "UTC")
	private LocalDateTime  createdTime;

	private String message;

	private String permalinkUrl;

	private String fullPicture;

	private FacebookPostPlaceRes place;

	private FacebookPostAttachmentsRes attachments;

	@Builder
	public FacebookPostRes(
		@JsonProperty("id") String id,
		@JsonProperty("updated_time") LocalDateTime updatedTime,
		@JsonProperty("created_time") LocalDateTime createdTime,
		@JsonProperty("message") String message,
		@JsonProperty("permalink_url") String permalinkUrl,
		@JsonProperty("full_picture") String fullPicture,
		@JsonProperty("place") FacebookPostPlaceRes place,
		@JsonProperty("attachments") FacebookPostAttachmentsRes attachments
	) {
		this.id = id;
		this.updatedTime = updatedTime;
		this.createdTime = createdTime;
		this.message = message;
		this.permalinkUrl = permalinkUrl;
		this.fullPicture = fullPicture;
		this.place = place;
		this.attachments = attachments;
	}
}