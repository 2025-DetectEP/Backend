package com.olive.pribee.infra.api.facebook;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.olive.pribee.global.error.GlobalErrorCode;
import com.olive.pribee.global.error.exception.AppException;
import com.olive.pribee.infra.api.facebook.dto.res.auth.FacebookAuthRes;
import com.olive.pribee.infra.api.facebook.dto.res.auth.FacebookTokenRes;
import com.olive.pribee.infra.api.facebook.dto.res.auth.FacebookUserInfoRes;
import com.olive.pribee.infra.api.facebook.dto.res.post.FacebookPostListRes;
import com.olive.pribee.infra.api.facebook.dto.res.post.FacebookPostRes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FacebookApiService {

	private final String FB_EXCHANGE_TOKEN = "fb_exchange_token";

	@Value("${url.facebook}")
	private String FACEBOOK_BASE_URL;

	@Value("${facebook.clientId}")
	private String FACEBOOK_CLIENT_ID;

	@Value("${facebook.clientSecret}")
	private String FACEBOOK_CLIENT_SECRET;

	@Value("${facebook.redirect-uri}")
	private String FACEBOOK_REDIRECT_URI;

	private final WebClient.Builder webClientBuilder;

	// Facebook WebClient 생성
	private WebClient getFacebookWebClient() {
		return webClientBuilder.baseUrl(FACEBOOK_BASE_URL).build();
	}

	public Mono<FacebookAuthRes> getFacebookIdWithToken(String code) {
		return exchangeCodeForAccessToken(code) // (1)
			.flatMap(shortLivedToken ->
				exchangeForLongTermAccessToken(shortLivedToken) // (2)
					.flatMap(longTermToken ->
						fetchFacebookId(longTermToken) // (3)
							.map(facebookId -> new FacebookAuthRes(facebookId, longTermToken))
					)
			)
			.onErrorResume(Exception.class, ex -> {
				if (ex instanceof AppException) {
					return Mono.error(ex);
				}

				log.error("[Facebook] Facebook ID & Long Term Token 가져오기 실패: {}", ex.getMessage(), ex);
				return Mono.error(new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR));
			});
	}

	// (1) Facebook Authorization Code → Short-Lived Access Token 변환
	private Mono<String> exchangeCodeForAccessToken(String code) {
		return getFacebookWebClient().get()
			.uri(uriBuilder -> uriBuilder
				.path("/oauth/access_token")
				.queryParam("client_id", FACEBOOK_CLIENT_ID)
				.queryParam("client_secret", FACEBOOK_CLIENT_SECRET)
				.queryParam("redirect_uri", FACEBOOK_REDIRECT_URI)
				.queryParam("code", code)
				.build())
			.retrieve()
			.onStatus(status ->
				status == HttpStatus.UNAUTHORIZED || status == HttpStatus.BAD_REQUEST, response -> {
				log.error("[Facebook] Invalid Facebook Code: {}", code);
				return Mono.error(new AppException(GlobalErrorCode.INVALID_FACEBOOK_CODE));
			})
			.bodyToMono(FacebookTokenRes.class)
			.map(FacebookTokenRes::getAccessToken) // Access Token 추출
			.onErrorResume(Exception.class, ex -> {
				if (ex instanceof AppException) {
					return Mono.error(ex);
				}

				log.error("[Facebook] AccessToken 요청 실패: {}", ex.getMessage(), ex);
				return Mono.error(new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR));
			});
	}

	// (2) Short-Lived Token → Long-Term Token 변환
	private Mono<String> exchangeForLongTermAccessToken(String shortLivedToken) {
		return getFacebookWebClient().get()
			.uri(uriBuilder -> uriBuilder
				.path("/oauth/access_token")
				.queryParam("grant_type", FB_EXCHANGE_TOKEN)
				.queryParam("client_id", FACEBOOK_CLIENT_ID)
				.queryParam("client_secret", FACEBOOK_CLIENT_SECRET)
				.queryParam("fb_exchange_token", shortLivedToken)
				.build())
			.retrieve()
			.bodyToMono(FacebookTokenRes.class)
			.map(FacebookTokenRes::getAccessToken)
			.onErrorResume(Exception.class, ex -> {
				log.error("[Facebook] Long Term AccessToken 요청 실패: {}", ex.getMessage(), ex);
				return Mono.error(new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR));
			});
	}

	// (3) Long-Term Token으로 Facebook ID 조회
	private Mono<String> fetchFacebookId(String accessToken) {
		return getFacebookWebClient().get()
			.uri(uriBuilder -> uriBuilder
				.path("/me")
				.queryParam("fields", "id")
				.queryParam("access_token", accessToken)
				.build())
			.retrieve()
			.bodyToMono(JsonNode.class)
			.map(json -> json.get("id").asText()) // JSON에서 id 값 추출
			.onErrorResume(Exception.class, ex -> {
				log.error("[Facebook] Facebook ID 조회 실패: {}", ex.getMessage(), ex);
				return Mono.error(new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR));
			});
	}

	// (4) Facebook 사용자 정보 조회
	public Mono<FacebookUserInfoRes> fetchFacebookUserInfo(String accessToken) {
		return getFacebookWebClient().get()
			.uri(uriBuilder -> uriBuilder
				.path("/me")
				.queryParam("fields", "id,name,email,picture.width(500).height(500){url}")
				.queryParam("access_token", accessToken)
				.build())
			.retrieve()
			.bodyToMono(FacebookUserInfoRes.class)
			.onErrorResume(Exception.class, ex -> {
				log.error("[Facebook] Facebook 사용자 정보 조회 실패: {}", ex.getMessage(), ex);
				return Mono.error(new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR));
			});
	}

	// (5) Facebook 게시물 조회
	public Mono<List<FacebookPostRes>> fetchFacebookUserPosts(String accessToken, LocalDateTime sinceTime) {
		return getFacebookWebClient().get()
			.uri(uriBuilder -> uriBuilder
				.path("/me")
				.queryParam("fields",
					"feed.limit(100){id,updated_time,created_time,message,permalink_url,full_picture,place{name},attachments{subattachments{media{image{src}}},type}}")
				.queryParam("access_token", accessToken)
				.build())
			.retrieve()
			.bodyToMono(FacebookPostListRes.class) // 전체 피드를 받아옴
			.flatMapMany(feed -> Flux.fromIterable(feed.getPosts().getPosts())) // 리스트로 변환하여 Flux로 반환
			.filter(facebookPostRes -> isAfterSinceTime(facebookPostRes, sinceTime)) // 시간 필터링
			.filter(this::isValidAttachment) // 유효한 첨부 필터링
			.collectList()
			.onErrorResume(Exception.class, ex -> {
				log.error("[Facebook] 게시물 조회 실패: {}", ex.getMessage(), ex);
				return Mono.error(new AppException(GlobalErrorCode.INTERNAL_SERVER_ERROR));
			});
	}

	// 지정한 시간 이후의 게시물인지 확인
	private boolean isAfterSinceTime(FacebookPostRes facebookPostRes, LocalDateTime sinceTime) {
		if (sinceTime == null) {
			return true;
		}
		LocalDateTime postCreatedTime = facebookPostRes.getCreatedTime().atZone(ZoneOffset.UTC).toLocalDateTime();
		return postCreatedTime.isAfter(sinceTime);
	}

	// 유효한 첨부파일인지 확인 (null이거나, album/photo 타입만 허용)
	private boolean isValidAttachment(FacebookPostRes facebookPostRes) {
		if (facebookPostRes.getAttachments() == null) {
			return true;
		}
		return facebookPostRes.getAttachments().getData().stream()
			.findFirst()
			.map(attachment -> "album".equals(attachment.getType()) || "photo".equals(attachment.getType()))
			.orElse(false);
	}

}
