package com.olive.pribee.infra.api.google.dlp;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(DlpReq.ofText(text))
			.retrieve()
			.bodyToMono(DlpRes.class)
			.doOnNext(res -> log.info("DLP API Response: {}", res))
			.map(DlpRes::getFindings);
	}

	public Mono<List<DlpFinding>> analyzeImage(String imageUrl) {
		return getDlpWebClient().post()
			.contentType(MediaType.APPLICATION_JSON)
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

}
