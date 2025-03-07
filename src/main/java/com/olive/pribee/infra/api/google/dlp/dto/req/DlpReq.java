package com.olive.pribee.infra.api.google.dlp.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.olive.pribee.global.enums.DetectKeyword;

import java.util.List;
import java.util.Map;

import static com.olive.pribee.global.util.fileUtil.encodeImageToBase64;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record DlpReq(
	@JsonProperty("item") Item item,
	@JsonProperty("inspectConfig") InspectConfig inspectConfig
) {
	public static DlpReq ofText(String value) {
		DlpReq req = new DlpReq(
			new Item(value),
			new InspectConfig(
				DetectKeyword.getInfoTypes().stream().map(InfoType::new).toList(),
				"POSSIBLE"
			)
		);
		return req;
	}

	public static DlpReq ofImage(String imageUrl) {
		return new DlpReq(
			new Item(Map.of("type", "IMAGE", "data", imageUrl)),
			new InspectConfig(
				DetectKeyword.getInfoTypes().stream().map(InfoType::new).toList(),
				"POSSIBLE"
			)
		);
	}
}

record Item(@JsonProperty("value") String value, @JsonProperty("byteItem") Map<String, String> byteItem) {
	public Item(String value) {
		this(value, null);
	}
	public Item(Map<String, String> byteItem) {
		this(null, byteItem);
	}
}

record InspectConfig(
	@JsonProperty("infoTypes") List<InfoType> infoTypes,
	@JsonProperty("minLikelihood") String minLikelihood,
	@JsonProperty("limits") Map<String, Integer> limits,
	@JsonProperty("includeQuote") boolean includeQuote
) {
	public InspectConfig(List<InfoType> infoTypes, String minLikelihood) {
		this(infoTypes, minLikelihood, Map.of("maxFindingsPerItem", 0), true);
	}
}

record InfoType(@JsonProperty("name") String name) {}