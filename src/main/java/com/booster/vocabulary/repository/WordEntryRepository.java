package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.WordEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordEntryRepository extends JpaRepository<WordEntryEntity, Long> {
}
