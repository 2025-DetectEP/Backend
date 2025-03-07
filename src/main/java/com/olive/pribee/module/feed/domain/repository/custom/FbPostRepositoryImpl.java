package com.olive.pribee.module.feed.domain.repository.custom;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.olive.pribee.global.enums.DetectKeyword;
import com.olive.pribee.module.feed.domain.entity.DetectKeywordInMessage;
import com.olive.pribee.module.feed.domain.entity.FbPost;
import com.olive.pribee.module.feed.domain.entity.FbPostPictureUrl;
import com.olive.pribee.module.feed.domain.entity.QDetectKeywordInMessage;
import com.olive.pribee.module.feed.domain.entity.QDetectKeywordInPhoto;
import com.olive.pribee.module.feed.domain.entity.QFbPost;
import com.olive.pribee.module.feed.domain.entity.QFbPostPictureUrl;
import com.olive.pribee.module.feed.dto.res.FbPostRes;
import com.olive.pribee.module.feed.dto.res.FbPostTotalRes;
import com.querydsl.core.BooleanBuilder;
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

        // 4. fbPostPictureUrls 조회 (Batch Fetch)
        Map<Long, List<FbPostPictureUrl>> pictureUrlMap = jpaQueryFactory
            .selectFrom(QFbPostPictureUrl.fbPostPictureUrl)
            .where(QFbPostPictureUrl.fbPostPictureUrl.fbPost.id.in(postIds))
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
}