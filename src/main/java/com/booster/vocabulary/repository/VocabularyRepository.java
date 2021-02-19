package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.VocabularyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<VocabularyEntity, String> {

    List<VocabularyEntity> findByUserIdAndLanguageToLearnId(String userId, String languageToLearnId);

    boolean existsByUserIdAndName(String userId, String name);

    Optional<VocabularyEntity> findByUserIdAndName(String userId, String name);

}
