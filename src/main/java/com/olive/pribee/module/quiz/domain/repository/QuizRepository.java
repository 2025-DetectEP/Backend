package com.olive.pribee.module.quiz.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olive.pribee.module.quiz.domain.entity.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
	@Query(value = "SELECT * FROM quiz " +
		"WHERE id >= (SELECT FLOOR(RAND() * (SELECT MAX(id) FROM quiz))) " +
		"ORDER BY id LIMIT 3", nativeQuery = true)
	List<Quiz> findTop3RandomOptimized();

}
