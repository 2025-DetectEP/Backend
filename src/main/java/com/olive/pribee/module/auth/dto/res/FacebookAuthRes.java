package com.olive.pribee.module.auth.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookAuthRes {

	private String facebookId;
	private String longTermToken;

	@Builder
	public FacebookAuthRes(
		@JsonProperty("facebookId") String facebookId,
		@JsonProperty("longTermToken") String longTermToken

	) {
		this.facebookId = facebookId;
		this.longTermToken = longTermToken;
	}
}