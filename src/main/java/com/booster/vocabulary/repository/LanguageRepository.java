package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {

    Optional<LanguageEntity> findByName(String name);

    boolean existsByName(String name);

}
