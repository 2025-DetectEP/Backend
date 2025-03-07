package com.olive.pribee.infra.api.facebook.dto.res.post;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookPostPlaceRes {
	private String name;

	@Builder
	public FacebookPostPlaceRes(@JsonProperty("name") String name) {
		this.name = name;
	}
}