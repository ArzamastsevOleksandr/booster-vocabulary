package com.booster.vocabulary.config.init;

import com.booster.vocabulary.entity.BaseLanguageEntity;
import com.booster.vocabulary.repository.BaseLanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static com.booster.vocabulary.util.StringUtil.randomUuid;

// TODO: SHOULD BE POPULATED VIA SEPARATE SQL-INSERT SCRIPT AT STARTUP (FLYWAY)
@Profile("!test")
@Component
@RequiredArgsConstructor
public class BaseLanguageListInitializer {

    private final BaseLanguageRepository baseLanguageRepository;

    @EventListener
    @Transactional
    public void handle(ContextRefreshedEvent event) {
        Arrays.stream(BaseLanguageEnum.values())
                .map(BaseLanguageEnum::toString)
                .filter(name -> !baseLanguageRepository.existsByName(name))
                .forEach(name -> {
                    var baseLanguageEntity = new BaseLanguageEntity();
                    baseLanguageEntity.setId(randomUuid());
                    baseLanguageEntity.setName(name);
                    baseLanguageRepository.save(baseLanguageEntity);
                });
    }

}
