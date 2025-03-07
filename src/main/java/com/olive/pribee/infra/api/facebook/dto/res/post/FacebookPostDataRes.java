package com.olive.pribee.infra.api.facebook.dto.res.post;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookPostDataRes {
	@JsonProperty("data")
	private List<FacebookPostRes> posts;
}