package com.olive.pribee.module.auth.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookUserInfoRes {
	private String id;
	private String name;
	private String email;
	private FacebookUserInfoPictureRes picture;

	@Builder
	public FacebookUserInfoRes(
		@JsonProperty("id") String id,
		@JsonProperty("name") String name,
		@JsonProperty("email") String email,
		@JsonProperty("picture") FacebookUserInfoPictureRes picture
	) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.picture = picture;
	}

}
