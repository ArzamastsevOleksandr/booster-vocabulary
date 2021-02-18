package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.VocabularyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import static com.booster.vocabulary.entity.VocabularyEntity.DEFAULT_VOCABULARY_NAME;

public interface VocabularyRepository extends JpaRepository<VocabularyEntity, String> {

    List<VocabularyEntity> findByUserIdAndLanguageToLearnId(String userId, String languageToLearnId);

    boolean existsByUserIdAndName(String userId, String name);

    Optional<VocabularyEntity> findByUserIdAndName(String userId, String name);

    default Optional<VocabularyEntity> findDefaultVocabulary(String userId) {
        return findByUserIdAndName(userId, DEFAULT_VOCABULARY_NAME);
    }

}
