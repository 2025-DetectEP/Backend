package com.olive.pribee.infra.api.facebook.dto.res.post;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookPostImageRes {
	private FacebookPostImageDataRes image;

	@Builder
	public FacebookPostImageRes(@JsonProperty("image") FacebookPostImageDataRes image) {
		this.image = image;
	}
}