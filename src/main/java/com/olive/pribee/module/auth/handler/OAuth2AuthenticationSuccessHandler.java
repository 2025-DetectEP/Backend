package com.olive.pribee.module.auth.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.olive.pribee.global.enums.JwtVo;
import com.olive.pribee.global.util.RedisUtil;
import com.olive.pribee.global.util.ResponseUtil;
import com.olive.pribee.module.auth.JwtTokenProvider;
import com.olive.pribee.module.auth.domain.entity.CustomOAuth2User;
import com.olive.pribee.module.auth.domain.entity.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisUtil redisUtil;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		CustomOAuth2User oAuth2User = (CustomOAuth2User)authentication.getPrincipal();
		Member member = oAuth2User.member();

		// JWT 생성
		JwtVo jwtVo = jwtTokenProvider.generateTokens(member);

		// Redis에 토큰 저장
		redisUtil.setOpsForValue(member.getId() + "_refresh", jwtVo.getRefreshToken(),
			jwtTokenProvider.getREFRESH_TOKEN_EXPIRATION());

		// 클라이언트에 응답
		ResponseUtil.setDataResponse(response, HttpServletResponse.SC_OK, jwtVo);

		clearAuthenticationAttributes(request);
	}
}
