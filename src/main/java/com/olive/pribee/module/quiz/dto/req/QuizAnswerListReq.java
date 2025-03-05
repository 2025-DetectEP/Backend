package com.olive.pribee.module.quiz.dto.req;

import java.util.List;

import org.hibernate.validator.constraints.UniqueElements;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Schema(description = "퀴즈 답변 체크 요청 DTO")
public record QuizAnswerListReq(
	@Schema(description = "요청하는 퀴즈 및 정답 여부 리스트", example = "[\n"
		+ "  {\n"
		+ "    \"id\": 1,\n"
		+ "    \"isCorrect\": false\n"
		+ "  },\n"
		+ "  {\n"
		+ "    \"id\": 3,\n"
		+ "    \"isCorrect\": true\n"
		+ "  },\n"
		+ "  {\n"
		+ "    \"id\": 4,\n"
		+ "    \"isCorrect\": false\n"
		+ "  }\n"
		+ "]")
	@NotEmpty
	@Size(min = 1, message = "퀴즈 답변 리스트는 최소 1개 이상이어야 합니다.")
	@UniqueElements
	List<QuizAnswerReq> quizAnswerReqs

) {
}