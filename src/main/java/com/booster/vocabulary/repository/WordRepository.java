package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.WordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WordRepository extends JpaRepository<WordEntity, String> {

    Optional<WordEntity> findByName(String word);

}
