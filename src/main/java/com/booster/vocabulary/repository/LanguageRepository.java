package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.LanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {

    boolean existsByName(String name);

}
