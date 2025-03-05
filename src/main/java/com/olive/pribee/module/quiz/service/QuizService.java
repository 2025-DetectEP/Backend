package com.olive.pribee.module.quiz.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.olive.pribee.global.error.exception.AppException;
import com.olive.pribee.module.quiz.domain.entity.Quiz;
import com.olive.pribee.module.quiz.domain.repository.QuizRepository;
import com.olive.pribee.module.quiz.dto.req.QuizAnswerListReq;
import com.olive.pribee.module.quiz.dto.req.QuizAnswerReq;
import com.olive.pribee.module.quiz.dto.res.QuizRandomRes;
import com.olive.pribee.module.quiz.error.QuizErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizService {
	private final QuizRepository quizRepository;

	// 퀴즈 3개 가져오기
	public List<QuizRandomRes> getRandomThreeQuiz() {
		// quiz 랜덤 추출
		List<Quiz> quizzes = quizRepository.findTop3RandomOptimized();

		// dto 매핑
		return quizzes.stream().map(
			quiz -> QuizRandomRes.of(
				quiz.getId(),
				quiz.getQuestion(),
				quiz.getAnswer1(),
				quiz.getAnswer2(),
				quiz.getAnswerIsOne(),
				quiz.getReason(),
				quiz.getParticipate(),
				quiz.getWrong()
			)).collect(Collectors.toList());
	}

	// 퀴즈 답변 반영
	@Transactional
	public void postQuizWrongPortion(QuizAnswerListReq quizAnswerReqs) {
		// id 기반 모든 퀴즈 조회
		Map<Long, Quiz> quizMap = quizRepository.findAllById(
			quizAnswerReqs.quizAnswerReqs().stream().map(QuizAnswerReq::id).toList()
		).stream().collect(Collectors.toMap(Quiz::getId, quiz -> quiz));

		if (quizMap.size() != quizAnswerReqs.quizAnswerReqs().size()) {
			throw new AppException(QuizErrorCode.INVALID_QUIZ_ID);
		}

		// 참여 횟수 및 오답 수 증가 처리
		for (QuizAnswerReq req : quizAnswerReqs.quizAnswerReqs()) {
			Quiz quiz = quizMap.get(req.id());
			quiz.plusParticipate();
			if (!req.isCorrect()) {
				quiz.plusWrong();
			}
		}

		// 변경된 퀴즈 목록 저장
		quizRepository.saveAll(quizMap.values());
	}
}
