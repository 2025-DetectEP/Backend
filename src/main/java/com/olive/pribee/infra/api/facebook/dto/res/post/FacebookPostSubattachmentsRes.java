package com.olive.pribee.infra.api.facebook.dto.res.post;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookPostSubattachmentsRes {
	@JsonProperty("data")
	private List<FacebookPostMediaRes> data;

	@Builder
	public FacebookPostSubattachmentsRes(@JsonProperty("data") List<FacebookPostMediaRes> data) {
		this.data = data;
	}
}