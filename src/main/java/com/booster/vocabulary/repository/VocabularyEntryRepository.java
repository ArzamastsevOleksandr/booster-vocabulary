package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.VocabularyEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VocabularyEntryRepository extends JpaRepository<VocabularyEntryEntity, Long> {
}
