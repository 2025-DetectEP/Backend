package com.olive.pribee.module.feed.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olive.pribee.global.enums.DetectKeyword;
import com.olive.pribee.global.enums.DetectLikelihood;
import com.olive.pribee.global.error.GlobalErrorCode;
import com.olive.pribee.global.error.exception.AppException;
import com.olive.pribee.infra.api.facebook.FacebookApiService;
import com.olive.pribee.infra.api.facebook.dto.res.post.FacebookPostAttachmentDataRes;
import com.olive.pribee.infra.api.facebook.dto.res.post.FacebookPostRes;
import com.olive.pribee.infra.api.google.dlp.GoogleDlpApiService;
import com.olive.pribee.infra.api.google.dlp.dto.res.DlpFinding;
import com.olive.pribee.module.auth.domain.entity.Member;
import com.olive.pribee.module.auth.domain.repository.MemberRepository;
import com.olive.pribee.module.feed.domain.entity.DetectKeywordInMessage;
import com.olive.pribee.module.feed.domain.entity.DetectKeywordInPhoto;
import com.olive.pribee.module.feed.domain.entity.DetectKeywordInPhotoBoundingBox;
import com.olive.pribee.module.feed.domain.entity.FbPost;
import com.olive.pribee.module.feed.domain.entity.FbPostPictureUrl;
import com.olive.pribee.module.feed.domain.repository.DetectKeywordInMessageRepository;
import com.olive.pribee.module.feed.domain.repository.DetectKeywordInPhotoRepository;
import com.olive.pribee.module.feed.domain.repository.FbPostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FbPostService {
	private final FacebookApiService facebookApiService;
	private final GoogleDlpApiService googleDlpApiService;

	private final FbPostRepository fbPostRepository;
	private final MemberRepository memberRepository;
	private final DetectKeywordInMessageRepository detectKeywordInMessageRepository;
	private final DetectKeywordInPhotoRepository detectKeywordInPhotoRepository;

	// 비동기적으로 게시물 저장 처리
	@Transactional
	public void savePostsAsync(String accessToken, Long memberId, LocalDateTime sinceTime) {
		fetchAndSavePosts(accessToken, memberId, sinceTime)
			.onErrorResume(ex -> {
				log.error("[Facebook] 게시물 저장 실패: {}", ex.getMessage(), ex);
				return Mono.empty();
			})
			.subscribe();
	}

	// 게시물 저장 및 분석 API 호출 로직
	private Mono<Void> fetchAndSavePosts(String accessToken, Long memberId, LocalDateTime sinceTime) {
		Member member = memberRepository.findById(memberId).orElseThrow(
			() -> new AppException(GlobalErrorCode.USER_NOT_FOUND)
		);

		return facebookApiService.fetchFacebookUserPosts(accessToken, sinceTime)
			.flatMap(posts -> {
				List<FbPost> fbPosts = posts.stream()
					.map(facebookPostRes -> {
						FbPost fbPost = convertToFacebookPost(facebookPostRes, member);
						extractPictureUrls(facebookPostRes, fbPost);
						return fbPost;
					})
					.collect(Collectors.toList());

				fbPostRepository.saveAll(fbPosts);

				return Flux.fromIterable(fbPosts)
					.flatMap(this::detectInfoFromFbPost)
					.then();
			});
	}

	// dto에서 FbPost 엔티티 변환 로직
	private FbPost convertToFacebookPost(FacebookPostRes facebookPostRes, Member member) {
		return FbPost.of(
			member,
			facebookPostRes.getId(),
			facebookPostRes.getCreatedTime(),
			facebookPostRes.getUpdatedTime(),
			facebookPostRes.getMessage(),
			facebookPostRes.getPermalinkUrl(),
			facebookPostRes.getPlace() != null ? facebookPostRes.getPlace().getName() : null
		);
	}

	// 게시물 사진 리스트 저장을 위한 추출 로직
	@Transactional
	public void extractPictureUrls(FacebookPostRes facebookPostRes, FbPost fbPost) {
		List<FbPostPictureUrl> fbPostPictureUrls = new ArrayList<>();

		if (facebookPostRes.getAttachments() != null && !facebookPostRes.getAttachments().getData().isEmpty()) {
			for (FacebookPostAttachmentDataRes attachment : facebookPostRes.getAttachments().getData()) {
				if ("photo".equals(attachment.getType())) {
					// "photo" 타입이면 즉시 postRes.getFullPicture() 저장 후 종료
					fbPostPictureUrls.add(FbPostPictureUrl.of(fbPost, facebookPostRes.getFullPicture()));
					fbPost.addPictureUrls(fbPostPictureUrls);
					return;
				} else if ("album".equals(attachment.getType()) && attachment.getSubattachments() != null) {
					// "album" 타입이면 subattachments의 모든 src 저장
					fbPostPictureUrls.addAll(
						attachment.getSubattachments().getData().stream()
							.map(media -> FbPostPictureUrl.of(fbPost, media.getMedia().getImage().getSrc()))
							.toList()
					);
				}
			}
		} else if (facebookPostRes.getFullPicture() != null) {
			// 첨부 파일이 없고 FullPicture만 존재할 경우
			fbPostPictureUrls.add(FbPostPictureUrl.of(fbPost, facebookPostRes.getFullPicture()));
		}

		fbPost.addPictureUrls(fbPostPictureUrls);
	}

	// 게시물 별 개인정보 탐지 API 요청 및 응답 저장 로직
	private Mono<Void> detectInfoFromFbPost(FbPost post) {
		return Mono.when(
			googleDlpApiService.analyzeText(post.getMessage())
				.filter(findings -> !findings.isEmpty())
				.flatMap(findings -> saveDetectKeywordsInMessage(post, findings))
				.doOnNext(findings -> log.info("Message Findings: {}", findings))
				.then(),

			Flux.fromIterable(post.getFbPostPictureUrls())
				.flatMap(url -> googleDlpApiService.analyzeImage(url.getPhotoUrl())
					.filter(findings -> !findings.isEmpty())
					.flatMap(findings -> saveDetectKeywordsInPhoto(url, findings))
					.doOnNext(findings -> log.info("Image Findings for {}: {}", url, findings)))
				.then()
		);
	}

	// 메세지에서 탐지한 개인정보 엔티티에 저장 로직
	@Transactional
	public Mono<Void> saveDetectKeywordsInMessage(FbPost fbPost, List<DlpFinding> findings) {
		List<DetectKeywordInMessage> detectedMessages = findings.stream()
			.map(f -> DetectKeywordInMessage.of(
				fbPost,
				f.getQuote(),
				DetectKeyword.of(f.getInfoType().getName()),
				DetectLikelihood.of(f.getLikelihood()),
				f.getLocation().getCodepointRange().getStart(),
				f.getLocation().getCodepointRange().getEnd()
			))
			.collect(Collectors.toList());

		if (!detectedMessages.isEmpty()) {
			detectKeywordInMessageRepository.saveAll(detectedMessages);
			updateDangerScore(detectedMessages, fbPost);
		}

		return Mono.empty();
	}

	// 개인정보 탐지에 따른 각 항목 별 점수 게시물(FbPost)에 업데이트
	private void updateDangerScore(List<DetectKeywordInMessage> detectedMessages, FbPost fbPost) {
		int dangerScore = detectedMessages.stream()
			.mapToInt(detectKeywordInMessage -> detectKeywordInMessage.getKeyword().getScore())
			.sum();
		fbPost.updateDangerScore(dangerScore);

		// TODO 추후 계속 저장하는 형태가 아닐 수 있도록 수정하기
		fbPostRepository.save(fbPost);
	}

	// 사진에서 탐지된 정보 엔티티에 저장 로직
	@Transactional
	public Mono<Void> saveDetectKeywordsInPhoto(FbPostPictureUrl pictureUrl, List<DlpFinding> findings) {
		List<DetectKeywordInPhoto> detectedPhotos = findings.stream()
			.map(f -> {
					DetectKeywordInPhoto detectKeywordInPhoto = DetectKeywordInPhoto.of(
						pictureUrl,
						f.getQuote(),
						DetectKeyword.of(f.getInfoType().getName()),
						DetectLikelihood.of(f.getLikelihood())
					);

					// BoundingBox 데이터 변환 및 추가
					List<DetectKeywordInPhotoBoundingBox> boundingBoxes = f.getLocation()
						.getContentLocations()
						.get(0)
						.getImageLocation()
						.getBoundingBoxes()
						.stream()
						.map(b -> DetectKeywordInPhotoBoundingBox.of(
							detectKeywordInPhoto,
							b.getTop(),
							b.getLeft(),
							b.getWidth(),
							b.getHeight()
						))
						.collect(Collectors.toList());

					detectKeywordInPhoto.addBoundingBoxes(boundingBoxes);
					return detectKeywordInPhoto;
				}
			).collect(Collectors.toList());

		if (!detectedPhotos.isEmpty()) {
			detectKeywordInPhotoRepository.saveAll(detectedPhotos);
		}

		return Mono.empty();
	}

	// 최근 게시물 생성 시간 가져오는 로직(새로운 게시물 가져오는 기준점을 위해 사용)
	public LocalDateTime getRecentPostsCreateTime() {
		// TODO 추후에 어떤 값을 가져오는 게 좋을지 더 고민하기(updateTime, or createTime)
		// 수정될 게시물에 대한 반영도 필요하기에...
		Optional<FbPost> fbPost = fbPostRepository.findLatestPost();
		return fbPost.map(FbPost::getCreatedTime).orElse(null);
	}

}
