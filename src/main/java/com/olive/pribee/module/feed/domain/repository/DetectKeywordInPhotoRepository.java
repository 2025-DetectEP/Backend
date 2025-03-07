package com.olive.pribee.module.feed.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olive.pribee.module.feed.domain.entity.DetectKeywordInPhoto;

@Repository
public interface DetectKeywordInPhotoRepository extends JpaRepository<DetectKeywordInPhoto, String> {
}
