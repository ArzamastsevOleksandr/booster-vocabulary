package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.VocabularyEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VocabularyEntryRepository extends JpaRepository<VocabularyEntryEntity, String> {

    List<VocabularyEntryEntity> findAllByUserIdAndVocabularyId(String userId, String vocabularyId);

    boolean existsByUserIdAndTargetWordName(String userId, String word);

    Integer countAllByVocabularyId(String vocabularyId);

}
