package com.booster.vocabulary.repository;

import com.booster.vocabulary.entity.LanguageToLearnEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LanguageToLearnRepository extends JpaRepository<LanguageToLearnEntity, Long> {

    boolean existsByUserIdAndBaseLanguageId(Long userId, Long baseLanguageId);

    List<LanguageToLearnEntity> findAllByUserId(Long userId);

}
