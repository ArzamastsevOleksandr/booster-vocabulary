package com.booster.vocabulary.service;

import com.booster.vocabulary.entity.WordEntity;
import com.booster.vocabulary.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.booster.vocabulary.util.StringUtil.randomUuid;

@Slf4j
@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    public WordEntity getWordEntityByNameOrCreateAndSave(String word) {
        return wordRepository.findByName(word)
                .orElseGet(() -> {
                    var wordEntity = new WordEntity();
                    wordEntity.setId(randomUuid());
                    wordEntity.setName(word);
                    return wordRepository.save(wordEntity);
                });
    }

}
