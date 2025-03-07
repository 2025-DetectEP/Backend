package com.olive.pribee.infra.api.facebook.dto.res.post;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookPostMediaRes {
	private FacebookPostImageRes media;
	private String type;

	@Builder
	public FacebookPostMediaRes(
		@JsonProperty("media") FacebookPostImageRes media,
		@JsonProperty("type") String type
	) {
		this.media = media;
		this.type = type;
	}
}
