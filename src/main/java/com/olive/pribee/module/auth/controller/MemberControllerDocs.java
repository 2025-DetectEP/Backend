package com.olive.pribee.module.auth.controller;

import org.springframework.http.ResponseEntity;

import com.olive.pribee.global.common.ResponseDto;
import com.olive.pribee.module.auth.domain.entity.Member;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Member", description = "사용자 관련 API")
public interface MemberControllerDocs {

	@Operation(summary = "로그인", description = "facebook code 를 통해 로그인을 발급합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Created",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples =
				@ExampleObject(value = "{ \"code\": 201, \"message\": \"Created\" }")
			)
		),
		@ApiResponse(responseCode = "401", description = "인증에 실패하였습니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples =
				@ExampleObject(value = "{ \"code\": 401, \"message\": \"facebook code가 유효하지 않습니다.\" }")

			)
		)
	})
	ResponseEntity<ResponseDto> getLogin(String code);

	@Operation(summary = "accessToken 재발급", description = "리프레시 토큰을 이용해 엑세스 토큰을 발급합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Created",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples =
				@ExampleObject(value = "{ \"code\": 201, \"message\": \"Created\" }")
			)
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples = @ExampleObject(value = "{ \"code\": 400, \"message\": \"필수 요청 헤더가 누락되었습니다.\" }")
			)
		),
		@ApiResponse(responseCode = "401", description = "인증에 실패하였습니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples = @ExampleObject(value = "[" +
					"{ \"code\": 401, \"message\": \"인증에 실패하였습니다.\" }," +
					"{ \"code\": 401, \"message\": \"Token이 유효하지 않습니다.\" }," +
					"{ \"code\": 401, \"message\": \"Refresh Token이 필요합니다.\" }" +
					"]"
				)
			)
		),
		@ApiResponse(responseCode = "403", description = "접근이 허용되지 않습니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples = @ExampleObject(value = "{ \"code\": 403, \"message\": \"Token이 만료되었습니다.\" }")
			)
		)
	})
	ResponseEntity<ResponseDto> getAccessToken(String refreshToken);

	@Operation(summary = "로그아웃", description = "사용자 로그아웃을 진행합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Ok",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples =
				@ExampleObject(value = "{ \"code\": 200, \"message\": \"Ok\" }")
			)
		),
		@ApiResponse(responseCode = "401", description = "인증에 실패하였습니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples = @ExampleObject(value = "[" +
					"{ \"code\": 401, \"message\": \"인증에 실패하였습니다.\" }," +
					"{ \"code\": 401, \"message\": \"Token이 유효하지 않습니다.\" }," +
					"{ \"code\": 401, \"message\": \"Access Token이 필요합니다.\" }" +
					"]"
				)
			)
		),
		@ApiResponse(responseCode = "403", description = "접근이 허용되지 않습니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples = @ExampleObject(value = "{ \"code\": 403, \"message\": \"Token이 만료되었습니다.\" }")
			)
		),
	})
	ResponseEntity<ResponseDto> deleteRefreshToken(Member member);

	@Operation(summary = "탈퇴", description = "사용자 탈퇴를 진행합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "Ok",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples =
				@ExampleObject(value = "{ \"code\": 200, \"message\": \"Ok\" }")
			)
		),
		@ApiResponse(responseCode = "401", description = "인증에 실패하였습니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples = @ExampleObject(value = "[" +
					"{ \"code\": 401, \"message\": \"인증에 실패하였습니다.\" }," +
					"{ \"code\": 401, \"message\": \"Token이 유효하지 않습니다.\" }," +
					"{ \"code\": 401, \"message\": \"Access Token이 필요합니다.\" }," +
					"{ \"code\": 404, \"message\": \"해당하는 사용자를 찾을 수 없습니다.\" }" +
					"]"
				)
			)
		),
		@ApiResponse(responseCode = "403", description = "접근이 허용되지 않습니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples = @ExampleObject(value = "{ \"code\": 403, \"message\": \"Token이 만료되었습니다.\" }")
			)
		),
		@ApiResponse(responseCode = "404", description = "해당 자원을 찾을 수 없습니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples = @ExampleObject(value = "{ \"code\": 404, \"message\": \"해당하는 사용자를 찾을 수 없습니다.\" }")
			)
		)
	})
	ResponseEntity<ResponseDto> deleteMember(Member member);
}

