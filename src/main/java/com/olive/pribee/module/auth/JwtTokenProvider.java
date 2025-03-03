package com.olive.pribee.module.auth;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.olive.pribee.global.enums.JwtVo;
import com.olive.pribee.global.error.GlobalErrorCode;
import com.olive.pribee.global.error.exception.AppException;
import com.olive.pribee.global.util.RedisUtil;
import com.olive.pribee.module.auth.domain.entity.Member;
import com.olive.pribee.module.auth.domain.repository.MemberRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

	@Value("${jwt.issuer}")
	private String ISSUER;

	@Value("${jwt.secret}")
	private String JWT_SECRET_KEY;

	@Value("${jwt.access-token-expiration}")
	private int ACCESS_TOKEN_EXPIRATION;

	@Getter
	@Value("${jwt.refresh-token-expiration}")
	private int REFRESH_TOKEN_EXPIRATION;

	private static final String PAYLOAD_KEY_ID = "id";
	private static final String PAYLOAD_KEY_FACEBOOK_ID = "facebookId";

	private final MemberRepository memberRepository;
	private final RedisUtil redisUtil;

	// AccessToken & RefreshToken 생성
	public JwtVo generateTokens(Member member) {
		Map<String, Object> payloads = new LinkedHashMap<>();
		payloads.put(PAYLOAD_KEY_ID, member.getId());
		payloads.put(PAYLOAD_KEY_FACEBOOK_ID, member.getFacebookId());

		Date now = new Date();
		Date accessExp = new Date(now.getTime() + ACCESS_TOKEN_EXPIRATION);
		Date refreshExp = new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION);

		return new JwtVo(
			createToken(payloads, accessExp, "access"),
			createToken(payloads, refreshExp, "refresh")
		);
	}

	// JWT 생성 메서드
	private String createToken(Map<String, Object> payloads, Date expiration, String subject) {
		return Jwts.builder()
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setClaims(payloads)
			.setIssuer(ISSUER)
			.setIssuedAt(new Date())
			.setExpiration(expiration)
			.setSubject(subject)
			.signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY.getBytes())
			.compact();
	}

	// 토큰을 검증하고 사용자 정보를 반환
	@Transactional(readOnly = true)
	public Member validateToken(boolean isAccessToken, String header) throws AppException {
		String token = extractToken(header);
		Claims claims = parseToken(token);

		Long userId = claims.get(PAYLOAD_KEY_ID, Long.class);
		Member member = memberRepository.findById(userId)
			.orElseThrow(() -> new AppException(GlobalErrorCode.USER_NOT_FOUND));

		if (!isAccessToken) {
			String storedRefreshToken = redisUtil.getOpsForValue(member.getId() + "_refresh");
			if (storedRefreshToken == null || !storedRefreshToken.equals(token)) {
				throw new AppException(GlobalErrorCode.AUTHORIZATION_FAILED);
			}
		}

		return member;
	}

	// Authorization 헤더에서 Bearer 토큰 추출
	private String extractToken(String header) {
		if (header == null || !header.startsWith("Bearer ")) {
			throw new AppException(GlobalErrorCode.INVALID_TOKEN);
		}
		return header.substring(7);
	}

	// JWT 토큰에서 클레임(Claims) 추출
	private Claims parseToken(String token) {
		try {
			return Jwts.parser()
				.setSigningKey(JWT_SECRET_KEY.getBytes())
				.build()
				.parseClaimsJws(token)
				.getBody();
		} catch (JwtException e) {
			throw new AppException(GlobalErrorCode.INVALID_TOKEN);
		}
	}
}
