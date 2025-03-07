package com.olive.pribee.module.auth.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olive.pribee.global.enums.JwtVo;
import com.olive.pribee.global.error.ErrorCode;
import com.olive.pribee.global.error.GlobalErrorCode;
import com.olive.pribee.global.error.exception.AppException;
import com.olive.pribee.global.util.RedisUtil;
import com.olive.pribee.infra.api.facebook.FacebookApiService;
import com.olive.pribee.infra.api.facebook.dto.res.auth.FacebookAuthRes;
import com.olive.pribee.infra.api.facebook.dto.res.auth.FacebookUserInfoRes;
import com.olive.pribee.module.auth.JwtTokenProvider;
import com.olive.pribee.module.auth.domain.entity.Member;
import com.olive.pribee.module.auth.domain.repository.MemberRepository;
import com.olive.pribee.module.auth.dto.res.LoginRes;
import com.olive.pribee.module.auth.dto.res.LoginUserInfoRes;
import com.olive.pribee.module.feed.service.FbPostService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	private final FacebookApiService facebookApiService;
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisUtil redisUtil;
	private final MemberRepository memberRepository;
	private final FbPostService fbPostService;

	// facebook code 기반 facebook 로그인을 통한 접근 jwt 발급
	@Transactional
	public LoginUserInfoRes getAccessToken(String code) {
		// code 기반 facebook ID 조회
		FacebookAuthRes facebookAuthRes = facebookApiService.getFacebookIdWithToken(code).block();
		if (facebookAuthRes == null) {
			log.error("[Facebook] facebookAuthRes is null in memberService -- " + code);
			throw new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR);
		}

		// facebook ID 기반 DB에서 회원 조회
		Optional<Member> optionalMember = memberRepository.findByFacebookId(facebookAuthRes.getFacebookId());

		Member member;
		if (optionalMember.isEmpty()) {
			// 저장된 회원이 없으면 Facebook API에서 회원 정보 조회
			FacebookUserInfoRes userInfo = facebookApiService.fetchFacebookUserInfo(
				facebookAuthRes.getLongTermToken()).block();
			if (userInfo == null) {
				log.error(
					"[Facebook] facebook userInfo is null in memberService -- " + facebookAuthRes.getFacebookId());
				throw new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR);
			}

			member = memberRepository.save(Member.of(
				userInfo.getId(),
				userInfo.getName(),
				userInfo.getEmail(),
				userInfo.getPicture().getData().getUrl()
			));

			// 저장된 회원이 없으면 전체 게시물 가져오기
			fbPostService.savePostsAsync(facebookAuthRes.getLongTermToken(), member.getId(), null);

		} else {
			member = optionalMember.get();

			// 가장 최근 게시물의 createTime 이후의 게시물 가져오기
			// TODO 현재는 추가된 게시물만 처리하고 있음
			//  업데이트, 삭제된 게시물 반영에 대한 처리 추가 필요
			fbPostService.savePostsAsync(facebookAuthRes.getLongTermToken(), member.getId(),
				fbPostService.getRecentPostsCreateTime());
		}

		// facebook long live accessToken Redis 에 저장
		redisUtil.setOpsForValue(member.getId() + "_fb_access", facebookAuthRes.getLongTermToken(), 5184000);

		// JWT 토큰 생성 및 refreshToken 저장
		JwtVo jwtVo = jwtTokenProvider.generateTokens(member);
		redisUtil.setOpsForValue(member.getId() + "_refresh", jwtVo.getRefreshToken(),
			jwtTokenProvider.getREFRESH_TOKEN_EXPIRATION());

		return LoginUserInfoRes.of(
			jwtVo.getAccessToken(),
			jwtVo.getRefreshToken(),
			member.getName(),
			member.getProfilePictureUrl()
		);
	}

	// refresh token 으로 새로운 accessToken 발급
	@Transactional
	public LoginRes getNewAccessToken(String refreshToken) {
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

		return LoginRes.of(jwtVo.getAccessToken(), jwtVo.getRefreshToken());
	}

	// 로그아웃
	@Transactional
	public void deleteRefreshToken(Member member) {
		deleteMemberRedis(member);
	}

	// 탈퇴
	@Transactional
	public void deleteMember(Member member) {
		// 사용자 정보 삭제
		deleteMemberRedis(member);
		memberRepository.delete(member);
	}

	private void deleteMemberRedis(Member member) {
		redisUtil.delete(member.getId() + "_fb_access");
		redisUtil.delete(member.getId() + "_refresh");
	}
}
