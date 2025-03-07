package com.olive.pribee.module.feed.domain.entity;

import com.olive.pribee.global.common.BaseTime;

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
@Table(name = "detect_keyword_in_photo_bounding_box")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class DetectKeywordInPhotoBoundingBox extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "detect_keyword_in_photo_id", nullable = false)
	private DetectKeywordInPhoto detectKeywordInPhoto;

	@NotNull
	private int y_position;

	@NotNull
	private int x_position;

	@NotNull
	private int width;

	@NotNull
	private int height;

	public static DetectKeywordInPhotoBoundingBox of(@NotNull DetectKeywordInPhoto detectKeywordInPhoto, int y_position,
		int x_position, int width, int height) {
		return DetectKeywordInPhotoBoundingBox.builder()
			.detectKeywordInPhoto(detectKeywordInPhoto)
			.y_position(y_position)
			.x_position(x_position)
			.width(width)
			.height(height)
			.build();
	}

}
