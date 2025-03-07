package com.olive.pribee.infra.api.google.dlp.dto.res;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DlpRes {

	@JsonProperty("result")
	private Result result;

	@Builder
	public DlpRes(Result result) {
		this.result = result;
	}

	public List<DlpFinding> getFindings() {
		return result != null && result.findings != null ? result.findings : List.of();
	}


	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Result {
		@JsonProperty("findings")
		private List<DlpFinding> findings;

		@Builder
		public Result(List<DlpFinding> findings) {
			this.findings = findings;
		}
	}

}
