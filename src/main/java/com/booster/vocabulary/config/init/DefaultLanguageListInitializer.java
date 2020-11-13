package com.booster.vocabulary.config.init;

import com.booster.vocabulary.entity.LanguageEntity;
import com.booster.vocabulary.repository.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DefaultLanguageListInitializer {

    private final LanguageRepository languageRepository;

    @EventListener
    public void handle(ContextRefreshedEvent event) {
        Stream.of(
                "English",
                "German"
        )
                .filter(name -> !languageRepository.existsByName(name))
                .forEach(name -> {
                    var languageEntity = new LanguageEntity();
                    languageEntity.setName(name);
                    languageRepository.save(languageEntity);
                });
    }

}
