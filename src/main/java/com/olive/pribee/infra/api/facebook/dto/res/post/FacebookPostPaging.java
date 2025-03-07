package com.olive.pribee.infra.api.facebook.dto.res.post;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookPostPaging {
	private String previous;
	private String next;

	@Builder
	public FacebookPostPaging(
		@JsonProperty("previous") String previous,
		@JsonProperty("next") String next
	) {
		this.previous = previous;
		this.next = next;
	}
}