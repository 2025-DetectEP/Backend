package com.olive.pribee.module.feed.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.olive.pribee.global.common.BaseTime;
import com.olive.pribee.global.enums.DetectKeyword;
import com.olive.pribee.global.enums.DetectLikelihood;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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

@Getter
@Entity
@Table(name = "detect_keyword_in_photo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class DetectKeywordInPhoto extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "fb_posts_picture_url_id", nullable = false)
	private FbPostPictureUrl fbPostPictureUrl;

	private String detectWord;

	private DetectKeyword keyword;

	private DetectLikelihood likelihood;

	@OneToMany(mappedBy = "detectKeywordInPhoto", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private List<DetectKeywordInPhotoBoundingBox> boundingBoxes = new ArrayList<>();

	public static DetectKeywordInPhoto of(@NotNull FbPostPictureUrl fbPostPictureUrl,
		@NotNull String detectWord, @NotNull DetectKeyword keyword, @NotNull DetectLikelihood likelihood) {
		return DetectKeywordInPhoto.builder()
			.fbPostPictureUrl(fbPostPictureUrl)
			.detectWord(detectWord)
			.keyword(keyword)
			.likelihood(likelihood)
			.build();
	}

	public void addBoundingBoxes(List<DetectKeywordInPhotoBoundingBox> boundingBoxes) {
		this.boundingBoxes.addAll(boundingBoxes);
	}

}