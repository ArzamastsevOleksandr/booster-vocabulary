package com.booster.vocabulary.mapper;

import com.booster.vocabulary.dto.VocabularyDto;
import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.repository.VocabularyEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class VocabularyMapper {

    private final VocabularyEntryRepository vocabularyEntryRepository;
    private final VocabularyEntryMapper vocabularyEntryMapper;

    public VocabularyDto vocabularyEntity2VocabularyDto(VocabularyEntity vocabularyEntity) {
        List<VocabularyEntryDto> vocabularyEntryDtoList = vocabularyEntity.getVocabularyEntries()
                .stream()
                .map(vocabularyEntryMapper::vocabularyEntryEntity2VocabularyEntryDto)
                .collect(toList());

        return VocabularyDto.builder()
                .id(vocabularyEntity.getId())
                .name(vocabularyEntity.getName())
                .createdOn(vocabularyEntity.getCreatedOn())
                .entryCount(vocabularyEntryRepository.countAllByVocabularyId(vocabularyEntity.getId()))
                .vocabularyEntries(vocabularyEntryDtoList)
                .build();
    }

}
