package com.olive.pribee.module.feed.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mongodb.lang.Nullable;
import com.olive.pribee.global.common.DataResponseDto;
import com.olive.pribee.global.common.ResponseDto;
import com.olive.pribee.global.enums.DetectKeyword;
import com.olive.pribee.module.auth.domain.entity.Member;
import com.olive.pribee.module.feed.dto.res.FbPostDetailRes;
import com.olive.pribee.module.feed.dto.res.FbPostTotalRes;
import com.olive.pribee.module.feed.service.FbPostService;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/feed/")
@RequiredArgsConstructor
public class FbPostController implements FbPostControllerDocs {
	private final FbPostService fbPostService;

	// 개별 게시물 조회
	@GetMapping("/{id}")
	public ResponseEntity<ResponseDto> getDetailPost(@AuthenticationPrincipal Member member,
		@PathVariable(name = "id") Long postId) {
		FbPostDetailRes res = fbPostService.getDetailPost(member.getId(), postId);
		return ResponseEntity.status(200).body(DataResponseDto.of(res, 200));

	}

	// 전체 게시물 조회
	@GetMapping
	public ResponseEntity<ResponseDto> getTotalPost(
		@AuthenticationPrincipal Member member,
		@Schema(description = "필터를 의미합니다.") @RequestParam(name = "detectType") @Nullable DetectKeyword detectType,
		@Schema(description = "검색어를 의미합니다.") @RequestParam(name = "keyword") @Nullable String keyword,
		@Schema(description = "0번부터 시작합니다. 조회할 페이지 번호를 의미합니다.") @RequestParam(name = "page") int page,
		@Schema(description = "조회할 페이지 크기를 의미합니다.") @RequestParam(name = "size") int size) {
		FbPostTotalRes resDto = fbPostService.getTotalPost(member.getId(), detectType, keyword, page, size);
		return ResponseEntity.status(200).body(DataResponseDto.of(resDto, 200));
	}

	// 게시물 첨부 조회
	@PostMapping("/detect")
	public ResponseEntity<ResponseDto> postDetectPost(@RequestParam(name = "file") MultipartFile file,
		@RequestParam(name = "message") String message) {
		ObjectNode res = fbPostService.postDetectPost(file, message);
		return ResponseEntity.status(200).body(DataResponseDto.of(res, 200));

	}
}
