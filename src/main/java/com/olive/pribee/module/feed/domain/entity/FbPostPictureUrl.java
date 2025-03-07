package com.olive.pribee.module.feed.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.olive.pribee.global.common.BaseTime;

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

@Getter
@Entity
@Table(name = "fb_posts_picture_url")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class FbPostPictureUrl extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "fb_post_id", nullable = false)
	private FbPost fbPost;

	@Column(columnDefinition = "TEXT")
	private String photoUrl;

	@OneToMany(mappedBy = "fbPostPictureUrl", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Builder.Default
	private List<DetectKeywordInPhoto> detectKeywordInPhotos = new ArrayList<>();

	public static FbPostPictureUrl of(@NotNull FbPost fbPost, @NotNull String photoUrl) {
		return FbPostPictureUrl.builder()
			.fbPost(fbPost)
			.photoUrl(photoUrl)
			.build();
	}
}