package com.olive.pribee.module.auth.service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.olive.pribee.module.auth.domain.entity.CustomOAuth2User;
import com.olive.pribee.module.auth.domain.entity.Member;
import com.olive.pribee.module.auth.domain.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		String accessToken = userRequest.getAccessToken().getTokenValue();

		// 받아올 정보 정의
		String facebookId = oAuth2User.getAttribute("id");
		String facebookName = oAuth2User.getAttribute("name");
		String facebookEmail = oAuth2User.getAttribute("email");
		String profilePictureUrl = getProfilePictureUrl(oAuth2User.getAttributes());

		// 멤버 없다면 회원가입
		Member member = memberRepository.findByFacebookId(facebookId)
			.orElseGet(() -> createNewMember(facebookId, facebookName, facebookEmail, profilePictureUrl));

		return new CustomOAuth2User(member, oAuth2User.getAttributes());
	}

	// 프로필 사진 URL을 가져오는 헬퍼 메소드
	private String getProfilePictureUrl(Map<String, Object> attributes) {
		Map<String, Object> pictureData = (Map<String, Object>)attributes.get("picture");
		return pictureData != null ? (String)((Map<String, Object>)pictureData.get("data")).get("url") : null;
	}

	// 새로운 회원을 생성하는 메소드
	private Member createNewMember(String facebookId, String name, String email, String profilePictureUrl) {
		Member newMember = Member.of(facebookId, name, email, profilePictureUrl);
		return memberRepository.save(newMember);
	}
}
