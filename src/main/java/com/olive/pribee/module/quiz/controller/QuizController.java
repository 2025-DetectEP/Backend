package com.olive.pribee.module.quiz.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olive.pribee.global.common.DataResponseDto;
import com.olive.pribee.global.common.ResponseDto;
import com.olive.pribee.module.quiz.dto.req.QuizAnswerListReq;
import com.olive.pribee.module.quiz.dto.res.QuizRandomRes;
import com.olive.pribee.module.quiz.service.QuizService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/quiz/")
@RequiredArgsConstructor
public class QuizController implements QuizControllerDocs {
	private final QuizService quizService;

	@GetMapping
	public ResponseEntity<ResponseDto> getRandomThreeQuiz() {
		List<QuizRandomRes> resDto = quizService.getRandomThreeQuiz();
		return ResponseEntity.status(200).body(DataResponseDto.of(resDto, 200));
	}

	@PostMapping
	public ResponseEntity<ResponseDto> postQuizWrongPortion(@RequestBody @Valid QuizAnswerListReq quizAnswerReqs) {
		quizService.postQuizWrongPortion(quizAnswerReqs);
		return ResponseEntity.ok(ResponseDto.of(200));
	}

}
