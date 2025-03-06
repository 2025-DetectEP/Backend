package com.olive.pribee.module.quiz.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@Schema(description = "퀴즈 랜덤 제공 응답 DTO")
public class QuizRandomRes {
	@Schema(description = "퀴즈 id", example = "1")
	private final Long id;
	@Schema(description = "퀴즈 질문", example = "회사 임원으로부터 \"기밀 문서를 확인해 주세요\"라는 이메일이 왔다. 어떻게 해야 할까?")
	private final String question;
	@Schema(description = "답변 항목 1", example = "이메일 주소를 꼼꼼히 확인하고, 수상하면 별도 연락하여 진위를 확인한다.")
	private final String answer1;
	@Schema(description = "답변 항목 2", example = "상사 이메일이므로 즉시 문서를 다운로드하여 확인한다.")
	private final String answer2;
	@Schema(description = "답변 항목 1이 정답인지 여부", example = "true")
	private final Boolean answerIsOne;
	@Schema(description = "답변 설명", example = "타깃 스피어 피싱 공격은 특정 조직을 노리는 정교한 공격이므로, 반드시 진위를 확인해야 한다.")
	private final String reason;
	@Schema(description = "오답자 비율", example = "32.23")
	private final float wrongPortion;

	public static QuizRandomRes of(Long id, String question, String answer1, String answer2, Boolean answerIsOne,
		String reason, int participant, int wrong) {
		float wrongPortion = participant == 0 ? 0 : (float)wrong / participant * 100;

		return QuizRandomRes.builder()
			.id(id)
			.question(question)
			.answer1(answer1)
			.answer2(answer2)
			.answerIsOne(answerIsOne)
			.reason(reason)
			.wrongPortion(Math.round(wrongPortion * 100) / 100.0f)
			.build();
	}

}
