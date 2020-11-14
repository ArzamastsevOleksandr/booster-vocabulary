package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.VocabularyEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VocabularyEntryRepository extends JpaRepository<VocabularyEntryEntity, Long> {

    List<VocabularyEntryEntity> findAllByUserId(Long userId);

    boolean existsByUserIdAndTargetWordName(Long userId, String word);

    Integer countAllByVocabularyId(Long vocabularyId);

}
