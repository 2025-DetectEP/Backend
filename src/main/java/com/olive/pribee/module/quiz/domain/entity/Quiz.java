package com.olive.pribee.module.quiz.domain.entity;

import com.olive.pribee.global.common.BaseTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quiz")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Quiz extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	private String question;

	@NotNull
	private String answer1;

	@NotNull
	private String answer2;

	@NotNull
	private Boolean answerIsOne;

	@NotNull
	private String reason;

	@NotNull
	private int participate;

	@NotNull
	private int wrong;

	public static Quiz of(@NotNull String question, @NotNull String answer1, @NotNull String answer2,
		@NotNull Boolean answerIsOne, @NotNull String reason) {
		return Quiz.builder()
			.question(question)
			.answer1(answer1)
			.answer2(answer2)
			.answerIsOne(answerIsOne)
			.reason(reason)
			.participate(0)
			.wrong(0)
			.build();
	}

	public void plusParticipate() {
		this.participate += 1;
	}

	public void plusWrong() {
		this.wrong += 1;
	}
}
