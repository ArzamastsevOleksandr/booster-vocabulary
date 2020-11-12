package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.LanguageEnum;
import com.booster.vocabulary.entity.VocabularySetEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VocabularySetRepository extends JpaRepository<VocabularySetEntity, Long> {

    Optional<VocabularySetEntity> findByLanguageNameAndUserId(LanguageEnum languageName, Long userId);

}
