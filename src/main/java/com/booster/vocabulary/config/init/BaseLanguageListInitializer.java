package com.booster.vocabulary.config.init;

import com.booster.vocabulary.entity.BaseLanguageEntity;
import com.booster.vocabulary.repository.BaseLanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class BaseLanguageListInitializer {

    private final BaseLanguageRepository baseLanguageRepository;

    @EventListener
    public void handle(ContextRefreshedEvent event) {
        Stream.of(
                "English",
                "German"
        )
                .filter(name -> !baseLanguageRepository.existsByName(name))
                .forEach(name -> {
                    var languageEntity = new BaseLanguageEntity();
                    languageEntity.setName(name);
                    baseLanguageRepository.save(languageEntity);
                });
    }

}
