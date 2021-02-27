package com.booster.vocabulary.config;

import com.booster.vocabulary.entity.BaseLanguageEntity;
import com.booster.vocabulary.repository.BaseLanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.booster.vocabulary.util.StringUtil.randomUuid;

@Profile("test")
@Service
@RequiredArgsConstructor
public class TestBaseLanguageOperations {

    private final BaseLanguageRepository baseLanguageRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BaseLanguageEntity createBaseLanguageEntity(String name) {
        var baseLanguageEntity = new BaseLanguageEntity();
        baseLanguageEntity.setId(randomUuid());
        baseLanguageEntity.setName(name);
        return baseLanguageRepository.save(baseLanguageEntity);
    }

}
