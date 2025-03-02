package com.olive.pribee.module.auth.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olive.pribee.global.enums.JwtVo;
import com.olive.pribee.global.error.ErrorCode;
import com.olive.pribee.global.error.GlobalErrorCode;
import com.olive.pribee.global.error.exception.AppException;
import com.olive.pribee.global.util.RedisUtil;
import com.olive.pribee.module.auth.JwtTokenProvider;
import com.olive.pribee.module.auth.domain.entity.Member;
import com.olive.pribee.module.auth.domain.repository.MemberRepository;
import com.olive.pribee.module.auth.dto.res.LoginResDto;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisUtil redisUtil;
	private final MemberRepository memberRepository;

	// refresh token 으로 새로운 accessToken 발급
	@Transactional
	public LoginResDto getAccessToken(String refreshToken) {
		if (refreshToken.isBlank()) {
			throw new AppException(GlobalErrorCode.REFRESH_TOKEN_REQUIRED);
		}

		// refreshToken 유효성 검사 실행
		Member tokenMember;
		try {
			tokenMember = jwtTokenProvider.validateToken(false, refreshToken);
		} catch (JwtException e) {
			ErrorCode code =
				e instanceof ExpiredJwtException ? GlobalErrorCode.EXPIRED_JWT : GlobalErrorCode.INVALID_TOKEN;

			throw new AppException(code);
		}

		// JWT 토큰 생성 및 refreshToken 저장
		JwtVo jwtVo = jwtTokenProvider.generateTokens(tokenMember);
		redisUtil.setOpsForValue(tokenMember.getId() + "_refresh", jwtVo.getRefreshToken(),
			jwtTokenProvider.getREFRESH_TOKEN_EXPIRATION());

		return LoginResDto.of(jwtVo.getAccessToken(), jwtVo.getRefreshToken());
	}

	// 로그아웃
	@Transactional
	public void deleteRefreshToken(Member member) {
		// 사용자 refreshToken 삭제
		redisUtil.delete(member.getId() + "_refresh");
	}

	// 탈퇴
	@Transactional
	public void deleteMember(Member member) {
		// 사용자 정보 삭제
		memberRepository.delete(member);
	}

}
