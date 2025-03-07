package com.olive.pribee.global.common.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.olive.pribee.global.error.exception.AppException;
import com.olive.pribee.module.auth.JwtTokenProvider;
import com.olive.pribee.module.auth.domain.entity.Member;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws ServletException, IOException {
		String requestURI = request.getRequestURI();

		// swagger 인증 필터링 없이 처리
		if (requestURI.startsWith("/swagger-ui/") ||
			requestURI.startsWith("/webjars/") ||
			requestURI.startsWith("/v3/api-docs")) {
			chain.doFilter(request, response);
			return;
		}

		// accessToken 이 필요없는 경우 필터링 없이 처리
		if (requestURI.startsWith("/api/auth/token") ||
			requestURI.startsWith("/api/auth/login/facebook") ||
			requestURI.startsWith("/api/quiz/**")||
			requestURI.startsWith("/api/feed/detect")) {
			chain.doFilter(request, response);
			return;
		}

		// 요청 헤더에서 JWT 토큰을 추출
		String token = resolveToken(request);

		if (token != null) {
			try {
				// 토큰 검증 후 유효한 회원 객체 반환
				Member member = jwtTokenProvider.validateToken(true, "Bearer " + token);

				// Spring Security 인증 객체 설정
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(member, null,
					member.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (AppException e) {
				log.error("JWT 인증 실패: {}", e.getMessage());
			}
		}

		chain.doFilter(request, response);
	}

	// 요청 헤더에서 "Authorization"을 통해 JWT 토큰을 추출
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		return (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
			? bearerToken.substring(7) :
			null;
	}
}

