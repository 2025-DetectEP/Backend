package com.olive.pribee.infra.api.google.dlp;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.olive.pribee.infra.api.google.dlp.dto.req.DlpReq;
import com.olive.pribee.infra.api.google.dlp.dto.res.DlpFinding;
import com.olive.pribee.infra.api.google.dlp.dto.res.DlpRes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class GoogleDlpApiService {

	@Value("${url.dlp}")
	private String GOOGLE_DLP_BASE_URL;

	private final WebClient.Builder webClientBuilder;

	private WebClient getDlpWebClient() {
		return webClientBuilder.baseUrl(GOOGLE_DLP_BASE_URL).build();
	}

	public Mono<List<DlpFinding>> analyzeText(String text) {
		return getDlpWebClient().post()
			.bodyValue(DlpReq.ofText(text))
			.retrieve()
			.bodyToMono(DlpRes.class)
			.doOnNext(res -> log.info("DLP API Response: {}", res))
			.map(DlpRes::getFindings);
	}

	public Mono<List<DlpFinding>> analyzeImage(String imageUrl) {
		return getDlpWebClient().post()
			.bodyValue(DlpReq.ofImage(imageUrl))
			.retrieve()
			.bodyToMono(JsonNode.class) // JSON 응답 로그 찍기
			.doOnNext(res -> log.info("DLP API Raw Response: {}", res))
			.flatMap(jsonNode -> {
				try {
					DlpRes dlpRes = new ObjectMapper().treeToValue(jsonNode, DlpRes.class);
					return Mono.just(dlpRes);
				} catch (JsonProcessingException e) {
					log.error("Failed to parse DLP API response: {}", e.getMessage());
					return Mono.empty(); // 변환 실패 시 빈 Mono 반환
				}
			})
			.doOnNext(res -> log.info("DLP API Parsed Response: {}", res))
			.map(DlpRes::getFindings);
	}

	private final ObjectMapper objectMapper;

	// TODO 추후에 있는 dto 로 매핑하기
	public Mono<ObjectNode> analyzeRawData(String message, MultipartFile file) {
		try {
			String base64Image = Base64.getEncoder().encodeToString(file.getBytes());
			return analyzeText(message)
				.zipWith(analyzeImage(base64Image))
				.map(tuple -> {
					ObjectNode response = objectMapper.createObjectNode();
					ArrayNode textResults = objectMapper.createArrayNode();
					ArrayNode imageResults = objectMapper.createArrayNode();

					// 텍스트 데이터 필터링
					for (DlpFinding finding : tuple.getT1()) {
						ObjectNode textNode = objectMapper.createObjectNode();
						textNode.put("quote", finding.getQuote());
						textNode.put("infoType", finding.getInfoType().getName());
						textNode.put("likelihood", finding.getLikelihood());
						if (finding.getLocation() != null && finding.getLocation().getByteRange() != null) {
							textNode.put("start", finding.getLocation().getByteRange().getStart());
							textNode.put("end", finding.getLocation().getByteRange().getEnd());
						}
						textResults.add(textNode);
					}

					// 이미지 데이터 필터링
					for (DlpFinding finding : tuple.getT2()) {
						ObjectNode imageNode = objectMapper.createObjectNode();
						imageNode.put("quote", finding.getQuote());
						imageNode.put("infoType", finding.getInfoType().getName());
						imageNode.put("likelihood", finding.getLikelihood());
						if (finding.getLocation() != null && finding.getLocation().getContentLocations() != null) {
							ArrayNode boundingBoxes = objectMapper.createArrayNode();
							finding.getLocation().getContentLocations().forEach(contentLocation -> {
								if (contentLocation.getImageLocation() != null) {
									boundingBoxes.add(objectMapper.valueToTree(
										contentLocation.getImageLocation().getBoundingBoxes()));
								}
							});
							imageNode.set("imageLocation", boundingBoxes);
						}
						imageResults.add(imageNode);
					}

					response.set("textRes", textResults);
					response.set("imageRes", imageResults);
					return response;
				});
		} catch (IOException e) {
			log.error("Failed to process image file", e);
			return Mono.error(e);
		}
	}

}
