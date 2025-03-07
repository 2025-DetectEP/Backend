package com.olive.pribee.infra.api.facebook.dto.res.post;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookPostImageDataRes {
	private String src;

	@Builder
	public FacebookPostImageDataRes(@JsonProperty("src") String src) {
		this.src = src;
	}
}