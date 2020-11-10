package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.WordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WordRepository extends JpaRepository<WordEntity, Long> {
}
