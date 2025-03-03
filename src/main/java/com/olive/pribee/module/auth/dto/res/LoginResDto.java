package com.olive.pribee.module.auth.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class LoginResDto {
	@Schema(description = "accessToken", example = "eyJ0eXAiOiJKV1QiLCJhbGc...")
	private final String accessToken;

	@Schema(description = "refreshToken", example = "eyJ0eXAiOiJKV1QiLCJhbGc...")
	private final String refreshToken;

	public static LoginResDto of(String accessToken, String refreshToken) {
		return LoginResDto.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
