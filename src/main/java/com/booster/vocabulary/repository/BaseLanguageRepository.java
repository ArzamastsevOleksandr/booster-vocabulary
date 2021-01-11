package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.BaseLanguageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseLanguageRepository extends JpaRepository<BaseLanguageEntity, Long> {

    boolean existsByName(String name);

}
