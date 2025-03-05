package com.olive.pribee.module.auth.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LoginRes {
	@Schema(description = "accessToken", example = "eyJ0eXAiOiJKV1QiLCJhbGc...")
	private final String accessToken;

	@Schema(description = "refreshToken", example = "eyJ0eXAiOiJKV1QiLCJhbGc...")
	private final String refreshToken;

	public static LoginRes of(String accessToken, String refreshToken) {
		return LoginRes.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
