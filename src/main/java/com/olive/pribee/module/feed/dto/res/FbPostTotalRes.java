package com.olive.pribee.module.feed.dto.res;

import java.util.List;

import org.springframework.data.domain.Page;

import com.olive.pribee.global.common.DataPageMappingDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Facebook 게시물 전체 정보 응답 DTO")
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class FbPostTotalRes {

	@Schema(description = "전체 게시물 수")
	private long totalPostCount;

	@Schema(description = "감지된 게시물 수")
	private long detectPostCount;

	@Schema(description = "피해 위험도")
	private Double averageDangerScore;

	@Schema(description = "게시물 정보")
	private DataPageMappingDto<List<FbPostRes>> fbPostResPage;

	public static FbPostTotalRes of(
		long totalPostCount,
		long detectPostCount,
		Double averageDangerScore,
		Page<FbPostRes> fbPostResPage
	) {

		return FbPostTotalRes.builder()
			.totalPostCount(totalPostCount)
			.detectPostCount(detectPostCount)
			.averageDangerScore(averageDangerScore)
			.fbPostResPage(DataPageMappingDto.of(fbPostResPage.getContent(), fbPostResPage.getTotalElements(),
				fbPostResPage.getTotalPages(), fbPostResPage.getSize(), fbPostResPage.getNumberOfElements()))
			.build();
	}
}
