package com.olive.pribee.module.feed.domain.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.olive.pribee.global.enums.DetectKeyword;
import com.olive.pribee.module.feed.dto.res.FbPostDetailRes;
import com.olive.pribee.module.feed.dto.res.FbPostRes;
import com.olive.pribee.module.feed.dto.res.FbPostTotalRes;

@Repository
public interface FbPostRepositoryCustom {
	Page<FbPostRes> getFbPostByKeywordAndPaging(Long userId, DetectKeyword detectType, String keyword,
		Pageable pageable);

	FbPostTotalRes getFbPostTotal(Long memberId, DetectKeyword detectType, String keyword,
		Pageable pageable);

	FbPostDetailRes getFbPostDetail(Long memberId, Long fbPostId);
}
