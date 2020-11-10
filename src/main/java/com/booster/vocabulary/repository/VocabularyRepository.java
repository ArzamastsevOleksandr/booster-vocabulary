package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.VocabularyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyRepository extends JpaRepository<VocabularyEntity, Long> {
}
