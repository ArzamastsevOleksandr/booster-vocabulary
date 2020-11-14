package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.LanguageVocabularySetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageVocabularySetRepository extends JpaRepository<LanguageVocabularySetEntity, Long> {

    boolean existsByUserIdAndLanguageId(Long userId, Long languageId);

    List<LanguageVocabularySetEntity> findAllByUserId(Long userId);

    Optional<LanguageVocabularySetEntity> findByUserIdAndLanguageId(Long userId, Long languageId);

}
