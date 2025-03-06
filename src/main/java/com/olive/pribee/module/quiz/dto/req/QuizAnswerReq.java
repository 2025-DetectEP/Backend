package com.olive.pribee.module.quiz.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "퀴즈 답변 체크 요청 DTO")
public record QuizAnswerReq(
	@Schema(description = "요청하는 퀴즈 id", example = "1")
	@NotEmpty
	Long id,
	@Schema(description = "퀴즈 정답 여부", example = "true")
	@NotEmpty
	Boolean isCorrect

) {
}