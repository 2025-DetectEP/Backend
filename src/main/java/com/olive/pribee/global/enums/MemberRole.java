package com.olive.pribee.global.enums;

import java.util.Arrays;

import lombok.Getter;

@Getter
public enum MemberRole {
	USER("ROLE_USER", "일반 사용자 권한"),
	ADMIN("ROLE_ADMIN", "관리자 권한"),
	GUEST("GUEST", "게스트 권한");

	private final String code;
	private final String name;

	private MemberRole(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public static MemberRole of(String code) {
		return Arrays.stream(MemberRole.values())
			.filter(r -> r.getCode().equals(code))
			.findAny()
			.orElse(GUEST);
	}
}
