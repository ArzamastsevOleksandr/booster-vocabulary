package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.VocabularyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<VocabularyEntity, Long> {

    Optional<VocabularyEntity> findByUserIdAndName(Long id, String name);

}
