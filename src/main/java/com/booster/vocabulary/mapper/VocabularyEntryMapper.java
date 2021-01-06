package com.booster.vocabulary.mapper;

import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.entity.VocabularyEntryEntity;
import com.booster.vocabulary.entity.WordEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Component
public class VocabularyEntryMapper {

    public VocabularyEntryDto vocabularyEntryEntity2VocabularyEntryDto(VocabularyEntryEntity vocabularyEntryEntity) {
        return VocabularyEntryDto
                .builder()
                .id(vocabularyEntryEntity.getId())
                .targetWord(vocabularyEntryEntity.getTargetWord().getName())
                .createdOn(vocabularyEntryEntity.getCreatedOn())
                .correctAnswersCount(vocabularyEntryEntity.getCorrectAnswersCount())
                .synonyms(getWordNames(vocabularyEntryEntity::getSynonyms))
                .antonyms(getWordNames(vocabularyEntryEntity::getAntonyms))
                .build();
    }

    private List<String> getWordNames(Supplier<List<WordEntity>> supplier) {
        return supplier.get()
                .stream()
                .map(WordEntity::getName)
                .collect(toList());
    }

}
