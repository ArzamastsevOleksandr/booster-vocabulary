package com.booster.vocabulary.mapper;

import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.dto.WordDto;
import com.booster.vocabulary.entity.VocabularyEntryEntity;
import com.booster.vocabulary.entity.WordEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class VocabularyEntryMapper {

    private final WordMapper wordMapper;

    public VocabularyEntryDto entity2Dto(VocabularyEntryEntity vocabularyEntryEntity) {
        return VocabularyEntryDto
                .builder()
                .id(vocabularyEntryEntity.getId())
                .targetWord(vocabularyEntryEntity.getTargetWord().getName())
                .createdOn(vocabularyEntryEntity.getCreatedOn())
                .correctAnswersCount(vocabularyEntryEntity.getCorrectAnswersCount())
                .synonyms(getWordDtoList(vocabularyEntryEntity::getSynonyms))
                .antonyms(getWordDtoList(vocabularyEntryEntity::getAntonyms))
                .build();
    }

    private List<WordDto> getWordDtoList(Supplier<List<WordEntity>> supplier) {
        return supplier.get()
                .stream()
                .map(wordMapper::entity2Dto)
                .collect(toList());
    }

}
