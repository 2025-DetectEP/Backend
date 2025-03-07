package com.olive.pribee.module.feed.domain.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.olive.pribee.global.common.BaseTime;
import com.olive.pribee.module.auth.domain.entity.Member;

@Getter
@Entity
@Table(name = "fb_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class FbPost extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(unique = true)
	private String postId;

	@ManyToOne
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	private LocalDateTime createdTime;

	private LocalDateTime updatedTime;

	@Column(columnDefinition = "TEXT")
	private String message;

	@Column(columnDefinition = "TEXT")
	private String permalinkUrl;

	private String place;

	@Column(columnDefinition = "TEXT")
	private String safeMessage;


	private Integer dangerScore;

	@OneToMany(mappedBy = "fbPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Builder.Default
	private List<DetectKeywordInMessage> detectKeywordInMessages = new ArrayList<>();

	@OneToMany(mappedBy = "fbPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Builder.Default
	private List<FbPostPictureUrl> fbPostPictureUrls = new ArrayList<>();

	public static FbPost of(@NotNull Member member, @NotNull String postId, LocalDateTime createdTime,
		LocalDateTime updatedTime, String message, String permalinkUrl, String place) {
		return FbPost.builder()
			.member(member)
			.postId(postId)
			.createdTime(createdTime)
			.updatedTime(updatedTime)
			.message(message)
			.permalinkUrl(permalinkUrl)
			.place(place)
			.build();
	}

	public void addPictureUrls(List<FbPostPictureUrl> urls) {
		this.fbPostPictureUrls.addAll(urls);
	}

	public void updateDangerScore(Integer dangerScore){
		this.dangerScore = dangerScore;
	}
}