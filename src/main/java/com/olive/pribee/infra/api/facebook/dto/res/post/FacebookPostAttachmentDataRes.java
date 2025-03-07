package com.olive.pribee.infra.api.facebook.dto.res.post;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookPostAttachmentDataRes {
	private String type;
	private FacebookPostSubattachmentsRes subattachments;

	@Builder
	public FacebookPostAttachmentDataRes(
		@JsonProperty("type") String type,
		@JsonProperty("subattachments") FacebookPostSubattachmentsRes subattachments
	) {
		this.type = type;
		this.subattachments = subattachments;
	}
}