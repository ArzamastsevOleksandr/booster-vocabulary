package com.booster.vocabulary.service;

import com.booster.vocabulary.entity.WordEntity;
import com.booster.vocabulary.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    public WordEntity getWordEntityByNameOrCreateAndSave(String word) {
        return wordRepository.findByName(word)
                .orElseGet(() -> {
                    var newWordEntity = new WordEntity();
                    newWordEntity.setName(word);
                    wordRepository.save(newWordEntity);
                    return newWordEntity;
                });
    }

}
