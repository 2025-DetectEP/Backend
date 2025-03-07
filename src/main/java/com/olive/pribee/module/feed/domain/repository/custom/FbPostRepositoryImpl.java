package com.olive.pribee.module.feed.domain.repository.custom;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.olive.pribee.global.enums.DetectKeyword;
import com.olive.pribee.global.enums.DetectLikelihood;
import com.olive.pribee.global.error.exception.AppException;
import com.olive.pribee.module.feed.domain.entity.DetectKeywordInMessage;
import com.olive.pribee.module.feed.domain.entity.FbPost;
import com.olive.pribee.module.feed.domain.entity.FbPostPictureUrl;
import com.olive.pribee.module.feed.domain.entity.QDetectKeywordInMessage;
import com.olive.pribee.module.feed.domain.entity.QDetectKeywordInPhoto;
import com.olive.pribee.module.feed.domain.entity.QDetectKeywordInPhotoBoundingBox;
import com.olive.pribee.module.feed.domain.entity.QFbPost;
import com.olive.pribee.module.feed.domain.entity.QFbPostPictureUrl;
import com.olive.pribee.module.feed.dto.res.BoundingBoxRes;
import com.olive.pribee.module.feed.dto.res.FbPostDetailRes;
import com.olive.pribee.module.feed.dto.res.FbPostRes;
import com.olive.pribee.module.feed.dto.res.FbPostTotalRes;
import com.olive.pribee.module.feed.dto.res.MessageDetectRes;
import com.olive.pribee.module.feed.dto.res.PhotoDetectRes;
import com.olive.pribee.module.feed.dto.res.PhotoUrlRes;
import com.olive.pribee.module.feed.error.FbPostErrorCode;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FbPostRepositoryImpl implements FbPostRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<FbPostRes> getFbPostByKeywordAndPaging(Long memberId, DetectKeyword detectType, String keyword,
		Pageable pageable) {

		QFbPost fbPost = QFbPost.fbPost;
		QDetectKeywordInMessage detectKeywordInMessage = QDetectKeywordInMessage.detectKeywordInMessage;
		QDetectKeywordInPhoto detectKeywordInPhoto = QDetectKeywordInPhoto.detectKeywordInPhoto;

		BooleanBuilder builder = new BooleanBuilder();
		builder.and(fbPost.member.id.eq(memberId));

		if (detectType != null) {
			builder.and(detectKeywordInMessage.keyword.eq(detectType)
				.or(detectKeywordInPhoto.keyword.eq(detectType)));
		}

		if (StringUtils.hasText(keyword)) {
			builder.and(fbPost.message.containsIgnoreCase(keyword));
		}

		// 1. 페이징 적용하여 FbPost ID 조회
		List<Long> postIds = jpaQueryFactory.select(fbPost.id)
			.from(fbPost)
			.where(builder)
			.orderBy(fbPost.createdTime.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		if (postIds.isEmpty()) {
			return new PageImpl<>(List.of(), pageable, 0);
		}

		// 2. FbPost 엔티티 조회 (Batch Fetch 적용)
		List<FbPost> posts = jpaQueryFactory.selectFrom(fbPost)
			.where(fbPost.id.in(postIds))
			.fetch();

		// 3. detectKeywordInMessages 조회 (Batch Fetch)
		Map<Long, List<DetectKeywordInMessage>> detectKeywordMap = jpaQueryFactory
			.selectFrom(detectKeywordInMessage)
			.where(detectKeywordInMessage.fbPost.id.in(postIds))
			.fetch()
			.stream()
			.collect(Collectors.groupingBy(d -> d.getFbPost().getId()));

		// 4. fbPostPictureUrls 조회 (Batch Fetch) - 오래된 순으로 정렬
		Map<Long, List<FbPostPictureUrl>> pictureUrlMap = jpaQueryFactory
			.selectFrom(QFbPostPictureUrl.fbPostPictureUrl)
			.where(QFbPostPictureUrl.fbPostPictureUrl.fbPost.id.in(postIds))
			.orderBy(QFbPostPictureUrl.fbPostPictureUrl.createdAt.asc())
			.fetch()
			.stream()
			.collect(Collectors.groupingBy(p -> p.getFbPost().getId()));

		// 5. DTO 변환
		List<FbPostRes> resDtos = posts.stream().map(post -> FbPostRes.of(
			post.getId(),
			post.getCreatedTime(),
			pictureUrlMap.getOrDefault(post.getId(), List.of()).stream()
				.findFirst().map(FbPostPictureUrl::getPhotoUrl).orElse(null),
			detectKeywordMap.getOrDefault(post.getId(), List.of()).stream()
				.map(DetectKeywordInMessage::getKeyword)
				.distinct()
				.collect(Collectors.toList())
		)).collect(Collectors.toList());

		long total = jpaQueryFactory.select(fbPost.count())
			.from(fbPost)
			.where(builder)
			.fetchOne();

		return new PageImpl<>(resDtos, pageable, total);
	}

	@Override
	public FbPostTotalRes getFbPostTotal(Long memberId, DetectKeyword detectType, String keyword,
		Pageable pageable) {

		Page<FbPostRes> fbPostResPage = getFbPostByKeywordAndPaging(memberId, detectType, keyword,
			pageable);

		QFbPost fbPost = QFbPost.fbPost;

		// 전체 게시물 수
		long totalPosts = jpaQueryFactory.select(fbPost.count())
			.from(fbPost)
			.where(fbPost.member.id.eq(memberId))
			.fetchOne();

		// danger_score > 0인 게시물 수
		long dangerPosts = jpaQueryFactory.select(fbPost.count())
			.from(fbPost)
			.where(fbPost.member.id.eq(memberId).and(fbPost.dangerScore.gt(0)))
			.fetchOne();

		// danger_score 평균값 (전체 게시물 수가 0이면 0 반환)
		Double averageDangerScore = jpaQueryFactory.select(fbPost.dangerScore.avg())
			.from(fbPost)
			.where(fbPost.member.id.eq(memberId))
			.fetchOne();

		return FbPostTotalRes.of(
			totalPosts,
			dangerPosts,
			averageDangerScore != null ? averageDangerScore : 0.0,
			fbPostResPage
		);
	}

	@Override
	public FbPostDetailRes getFbPostDetail(Long memberId, Long fbPostId) {
		QFbPost fbPost = QFbPost.fbPost;
		QDetectKeywordInMessage detectKeywordInMessage = QDetectKeywordInMessage.detectKeywordInMessage;
		QDetectKeywordInPhoto detectKeywordInPhoto = QDetectKeywordInPhoto.detectKeywordInPhoto;
		QFbPostPictureUrl fbPostPictureUrl = QFbPostPictureUrl.fbPostPictureUrl;
		QDetectKeywordInPhotoBoundingBox boundingBox = QDetectKeywordInPhotoBoundingBox.detectKeywordInPhotoBoundingBox;

		// 1. 게시물 기본 정보 조회
		FbPost post = jpaQueryFactory
			.selectFrom(fbPost)
			.where(fbPost.id.eq(fbPostId).and(fbPost.member.id.eq(memberId)))
			.fetchOne();

		if (post == null) {
			throw new AppException(FbPostErrorCode.INVALID_QUIZ_ID);
		}

		// 2. 메시지 내 감지된 개인정보 조회
		List<MessageDetectRes> messageDetectResList = jpaQueryFactory
			.selectFrom(detectKeywordInMessage)
			.where(detectKeywordInMessage.fbPost.id.eq(fbPostId))
			.fetch()
			.stream()
			.map(msg -> MessageDetectRes.of(
				msg.getDetectWord(),
				msg.getKeyword(),
				msg.getLikelihood(),
				msg.getStartAt(),
				msg.getEndAt()
			))
			.collect(Collectors.toList());

		// 3. 사진 URL 가져오기 (오래된 순 정렬)
		List<String> allPhotoUrls = jpaQueryFactory
			.select(fbPostPictureUrl.photoUrl)
			.from(fbPostPictureUrl)
			.where(fbPostPictureUrl.fbPost.id.eq(fbPostId))
			.orderBy(fbPostPictureUrl.createdAt.asc())
			.fetch();

		// 4. 사진 내 감지된 개인정보 조회
		List<Tuple> photoData = jpaQueryFactory
			.select(
				fbPostPictureUrl.photoUrl,
				detectKeywordInPhoto.detectWord,
				detectKeywordInPhoto.keyword,
				detectKeywordInPhoto.likelihood,
				boundingBox.x,
				boundingBox.y,
				boundingBox.width,
				boundingBox.height
			)
			.from(fbPostPictureUrl)
			.leftJoin(detectKeywordInPhoto).on(detectKeywordInPhoto.fbPostPictureUrl.id.eq(fbPostPictureUrl.id))
			.leftJoin(boundingBox).on(detectKeywordInPhoto.id.eq(boundingBox.detectKeywordInPhoto.id))
			.where(fbPostPictureUrl.fbPost.id.eq(fbPostId))
			.orderBy(fbPostPictureUrl.createdAt.asc())
			.fetch();

		// 5. 데이터를 `PhotoUrlRes`로 변환 (LinkedHashMap 사용)
		Map<String, List<PhotoDetectRes>> photoDetectMap = new LinkedHashMap<>();

		// 5-1. 모든 사진 URL을 우선 빈 리스트로 저장
		for (String pictureUrl : allPhotoUrls) {
			photoDetectMap.put(pictureUrl, new ArrayList<>());
		}

		// 5-2. 감지된 개인정보 정보 추가
		for (Tuple tuple : photoData) {
			String pictureUrl = tuple.get(fbPostPictureUrl.photoUrl);
			String detectWord = tuple.get(detectKeywordInPhoto.detectWord);
			DetectKeyword keyword = tuple.get(detectKeywordInPhoto.keyword);
			DetectLikelihood likelihood = tuple.get(detectKeywordInPhoto.likelihood);

			Integer x = tuple.get(boundingBox.x);
			Integer y = tuple.get(boundingBox.y);
			Integer width = tuple.get(boundingBox.width);
			Integer height = tuple.get(boundingBox.height);

			List<BoundingBoxRes> boundingBoxes = new ArrayList<>();
			if (x != null && y != null && width != null && height != null) {
				boundingBoxes.add(BoundingBoxRes.of(x, y, width, height));
			}

			if (detectWord != null || keyword != null || likelihood != null) {
				PhotoDetectRes photoDetectRes = PhotoDetectRes.of(detectWord, keyword, likelihood, boundingBoxes);
				photoDetectMap.get(pictureUrl).add(photoDetectRes); // 기존 리스트에 추가
			}
		}

		// 5-3. `PhotoUrlRes` 리스트 생성
		List<PhotoUrlRes> photoUrlResList = photoDetectMap.entrySet().stream()
			.map(entry -> PhotoUrlRes.of(entry.getKey(), entry.getValue())) // 빈 리스트 유지
			.collect(Collectors.toList());

		// 6. DTO 변환 후 반환
		return FbPostDetailRes.of(
			post.getCreatedTime(),
			post.getMessage(),
			post.getPermalinkUrl(),
			post.getSafeMessage(),
			messageDetectResList,
			photoUrlResList
		);
	}

}