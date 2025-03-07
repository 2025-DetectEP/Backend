package com.olive.pribee.infra.api.google.dlp.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DlpFinding {
	@JsonProperty("quote")
	private String quote;

	@JsonProperty("infoType")
	private InfoType infoType;

	@JsonProperty("likelihood")
	private String likelihood;

	@JsonProperty("location")
	private DlpLocation location;

	@JsonProperty("createTime")
	private String createTime;

	@JsonProperty("findingId")
	private String findingId;

	@Builder
	public DlpFinding(String quote, InfoType infoType, String likelihood, DlpLocation location, String createTime, String findingId) {
		this.quote = quote;
		this.infoType = infoType;
		this.likelihood = likelihood;
		this.location = location;
		this.createTime = createTime;
		this.findingId = findingId;
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class InfoType {
		@JsonProperty("name")
		private String name;

		@JsonProperty("sensitivityScore")
		private SensitivityScore sensitivityScore;

		@Builder
		public InfoType(String name, SensitivityScore sensitivityScore) {
			this.name = name;
			this.sensitivityScore = sensitivityScore;
		}
	}

	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SensitivityScore {
		@JsonProperty("score")
		private String score;

		@Builder
		public SensitivityScore(String score) {
			this.score = score;
		}
	}
}