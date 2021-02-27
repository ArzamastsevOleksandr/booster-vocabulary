package com.booster.vocabulary.config;

import com.booster.vocabulary.entity.BaseLanguageEntity;
import com.booster.vocabulary.entity.LanguageToLearnEntity;
import com.booster.vocabulary.entity.UserEntity;
import com.booster.vocabulary.repository.LanguageToLearnRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.booster.vocabulary.util.StringUtil.randomUuid;

@Profile("test")
@Service
@RequiredArgsConstructor
public class TestLanguageToLearnOperations {

    private final LanguageToLearnRepository languageToLearnRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LanguageToLearnEntity createLanguageToLearnEntity(BaseLanguageEntity baseLanguageEntity,
                                                             UserEntity userEntity) {
        var languageToLearnEntity = new LanguageToLearnEntity();
        languageToLearnEntity.setId(randomUuid());
        languageToLearnEntity.setBaseLanguage(baseLanguageEntity);
        languageToLearnEntity.setUser(userEntity);
        return languageToLearnRepository.save(languageToLearnEntity);
    }

}
