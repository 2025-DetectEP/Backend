package com.olive.pribee.infra.api.facebook.dto.res.post;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookPostAttachmentsRes {
	@JsonProperty("data")
	private List<FacebookPostAttachmentDataRes> data;

	@Builder
	public FacebookPostAttachmentsRes(List<FacebookPostAttachmentDataRes> data) {
		this.data = data;
	}
}