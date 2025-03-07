package com.olive.pribee.module.feed.domain.entity;

import com.olive.pribee.global.common.BaseTime;
import com.olive.pribee.global.enums.DetectKeyword;
import com.olive.pribee.global.enums.DetectLikelihood;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "detect_keyword_in_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class DetectKeywordInMessage extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "fb_post_id", nullable = false)
	private FbPost fbPost;

	private String detectWord;

	private DetectKeyword keyword;

	private DetectLikelihood likelihood;

	private Integer startAt;

	private Integer endAt;

	public static DetectKeywordInMessage of(@NotNull FbPost fbPost, @NotNull String detectWord,
		@NotNull DetectKeyword keyword,
		@NotNull DetectLikelihood likelihood, @NotNull Integer startAt, @NotNull Integer endAt) {
		return DetectKeywordInMessage.builder()
			.fbPost(fbPost)
			.detectWord(detectWord)
			.keyword(keyword)
			.likelihood(likelihood)
			.startAt(startAt)
			.endAt(endAt)
			.build();
	}

}