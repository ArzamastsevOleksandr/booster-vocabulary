package com.booster.vocabulary.config;

import com.booster.vocabulary.entity.BaseLanguageEntity;
import com.booster.vocabulary.repository.BaseLanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Profile("test")
@Service
public class TestBaseLanguageOperations {

    @Autowired
    private BaseLanguageRepository baseLanguageRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BaseLanguageEntity createAndSaveBaseLanguageEntity(String name) {
        var baseLanguageEntity = new BaseLanguageEntity();
        baseLanguageEntity.setName(name);
        return baseLanguageRepository.save(baseLanguageEntity);
    }

}
