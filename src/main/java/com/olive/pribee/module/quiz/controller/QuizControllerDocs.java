package com.olive.pribee.module.quiz.controller;

import org.springframework.http.ResponseEntity;

import com.olive.pribee.global.common.ResponseDto;
import com.olive.pribee.module.quiz.dto.req.QuizAnswerListReq;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Quiz", description = "퀴즈 관련 API")
public interface QuizControllerDocs {

	@Operation(summary = "랜덤 퀴즈 3개 추출 API", description = "")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "Created",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples =
				@ExampleObject(value = "{\n"
					+ "  \"code\": 200,\n"
					+ "  \"message\": \"OK\",\n"
					+ "  \"data\": [\n"
					+ "    {\n"
					+ "      \"id\": 3,\n"
					+ "      \"question\": \"생년월일을 SNS 프로필에서 비공개로 설정했지만, 아이디 또는 비밀번호에 생일과 연관된 숫자를 사용했다.\",\n"
					+ "      \"answer1\": \"괜찮다\",\n"
					+ "      \"answer2\": \"위험하다\",\n"
					+ "      \"answerIsOne\": false,\n"
					+ "      \"reason\": \"생년월일은 해킹 공격자가 비밀번호를 추측할 때 가장 먼저 시도하는 정보 중 하나다. SNS 비밀번호는 생일과 무관한 강력한 조합으로 설정하는 것이 안전하다.\",\n"
					+ "      \"wrongPortion\": 0\n"
					+ "    },\n"
					+ "    {\n"
					+ "      \"id\": 9,\n"
					+ "      \"question\": \"친구와 찍은 사진을 SNS에 올릴 때 친구에게 허락을 받지 않았다.\",\n"
					+ "      \"answer1\": \"괜찮다\",\n"
					+ "      \"answer2\": \"위험하다\",\n"
					+ "      \"answerIsOne\": false,\n"
					+ "      \"reason\": \"본인은 괜찮더라도 친구가 사진 공개를 원치 않을 수 있으며, 초상권 및 개인정보 보호 문제가 발생할 수 있다.\",\n"
					+ "      \"wrongPortion\": 0\n"
					+ "    },\n"
					+ "    {\n"
					+ "      \"id\": 18,\n"
					+ "      \"question\": \"회사 이메일로 온 첨부파일을 열기 전에 해야 할 행동은?\",\n"
					+ "      \"answer1\": \"발신자를 확인하고, 출처가 불분명하면 열지 않는다.\",\n"
					+ "      \"answer2\": \"업무 관련 내용이라면 바로 다운로드해서 실행한다.\",\n"
					+ "      \"answerIsOne\": true,\n"
					+ "      \"reason\": \"악성코드가 포함된 이메일 첨부파일은 기업 내 랜섬웨어 감염을 일으킬 수 있다.\",\n"
					+ "      \"wrongPortion\": 0\n"
					+ "    }\n"
					+ "  ]\n"
					+ "}")
			)
		)
	})
	ResponseEntity<ResponseDto> getRandomThreeQuiz();

	@Operation(summary = "정답률 처리 API", description = "id 와 정답 여부를 받아 정답률을 처리하는 API 입니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "OK",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples =
				@ExampleObject(value = "{ \"code\": 200, \"message\": \"OK\" }")
			)
		),
		@ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
			content = @Content(
				mediaType = "application/json",
				schema = @Schema(implementation = ResponseDto.class),
				examples = @ExampleObject(value = "{ \"code\": 400, \"message\": \"해당하는 퀴즈가 없습니다.\" }")
			)
		)
	})
	ResponseEntity<ResponseDto> postQuizWrongPortion(QuizAnswerListReq quizAnswerReqs);

}

