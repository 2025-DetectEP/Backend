package com.olive.pribee.global.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtVo {
    private final String accessToken;
    private final String refreshToken;
}