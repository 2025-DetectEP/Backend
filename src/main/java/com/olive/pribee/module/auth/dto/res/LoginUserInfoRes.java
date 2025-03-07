package com.olive.pribee.module.auth.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LoginUserInfoRes {
	@Schema(description = "accessToken", example = "eyJ0eXAiOiJKV1QiLCJhbGc...")
	private final String accessToken;

	@Schema(description = "refreshToken", example = "eyJ0eXAiOiJKV1QiLCJhbGc...")
	private final String refreshToken;

	@Schema(description = "이름", example = "올리비")
	private String name;

	@Schema(description = "사용자 프로필 사진", example = "eyJ0eXAiOiJKV1QiLCJhbGc...")
	private String profilePictureUrl;

	public static LoginUserInfoRes of(String accessToken, String refreshToken, String name, String profilePictureUrl) {
		return LoginUserInfoRes.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.name(name)
			.profilePictureUrl(profilePictureUrl)
			.build();
	}
}
