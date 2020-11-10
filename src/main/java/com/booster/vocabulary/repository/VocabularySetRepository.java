package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.VocabularySetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularySetRepository extends JpaRepository<VocabularySetEntity, Long> {
}
