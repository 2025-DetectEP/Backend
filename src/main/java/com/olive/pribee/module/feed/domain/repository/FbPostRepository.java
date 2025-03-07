package com.olive.pribee.module.feed.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.olive.pribee.module.feed.domain.entity.FbPost;

@Repository
public interface FbPostRepository extends JpaRepository<FbPost, String> {

	@Query("SELECT f FROM FbPost f ORDER BY f.createdTime DESC LIMIT 1")
	Optional<FbPost> findLatestPost();
}