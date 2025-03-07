package com.olive.pribee.infra.api.facebook.dto.res.post;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacebookPostListRes {
	@JsonProperty("feed")
	private FacebookPostDataRes posts; // 게시물 리스트
	private FacebookPostPaging paging;

	@Builder
	public FacebookPostListRes(FacebookPostDataRes posts, FacebookPostPaging paging) {
		this.posts = posts;
		this.paging = paging;
	}
}