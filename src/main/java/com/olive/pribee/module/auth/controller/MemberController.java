package com.olive.pribee.module.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olive.pribee.global.common.DataResponseDto;
import com.olive.pribee.global.common.ResponseDto;
import com.olive.pribee.module.auth.domain.entity.Member;
import com.olive.pribee.module.auth.dto.res.LoginRes;
import com.olive.pribee.module.auth.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth/")
@RequiredArgsConstructor
@Slf4j
public class MemberController implements MemberControllerDocs {
	private final MemberService memberService;

	@GetMapping("/login/facebook")
	public ResponseEntity<ResponseDto> getLogin(@RequestHeader("facebook-code") String code){
		LoginRes resDto = memberService.getAccessToken(code);
		return ResponseEntity.status(201).body(DataResponseDto.of(resDto, 201));
	}

	@GetMapping("/token")
	public ResponseEntity<ResponseDto> getAccessToken(@RequestHeader("Authorization-Refresh") String refreshToken) {
		LoginRes resDto = memberService.getNewAccessToken(refreshToken);
		return ResponseEntity.status(201).body(DataResponseDto.of(resDto, 201));
	}

	@DeleteMapping("/logout")
	public ResponseEntity<ResponseDto> deleteRefreshToken(@AuthenticationPrincipal Member member) {
		memberService.deleteRefreshToken(member);
		return ResponseEntity.ok(ResponseDto.of(200));
	}

	@DeleteMapping("/withdraw")
	public ResponseEntity<ResponseDto> deleteMember(@AuthenticationPrincipal Member member) {
		memberService.deleteMember(member);
		return ResponseEntity.ok(ResponseDto.of(200));
	}
}
