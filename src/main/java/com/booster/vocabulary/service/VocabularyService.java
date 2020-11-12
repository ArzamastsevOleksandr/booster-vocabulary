package com.booster.vocabulary.service;

import com.booster.vocabulary.dto.VocabularyDto;
import com.booster.vocabulary.dto.VocabularyEntryDto;
import com.booster.vocabulary.entity.VocabularyEntity;
import com.booster.vocabulary.exception.VocabularyEntityByIdNotFoundException;
import com.booster.vocabulary.repository.VocabularyEntryRepository;
import com.booster.vocabulary.repository.VocabularyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class VocabularyService {

    private final VocabularyRepository vocabularyRepository;
    private final VocabularyEntryRepository vocabularyEntryRepository;

    public List<VocabularyDto> findAllForUserId(Long userId) {
        List<VocabularyEntity> vocabularyEntityList = vocabularyRepository.findByUserId(userId);

        return vocabularyEntityList.stream()
                .map(vocabularyEntity ->
                        VocabularyDto.builder()
                                .id(vocabularyEntity.getId())
                                .name(vocabularyEntity.getName())
                                .createdOn(vocabularyEntity.getCreatedOn())
                                .entryCount(vocabularyEntryRepository.countAllByVocabularyId(vocabularyEntity.getId()))
                                .build()
                ).collect(toList());
    }

    public VocabularyDto findById(Long vocabularyId) {
        VocabularyEntity vocabularyEntity = vocabularyRepository.findById(vocabularyId)
                .orElseThrow(() -> new VocabularyEntityByIdNotFoundException(vocabularyId));

        return VocabularyDto.builder()
                .id(vocabularyEntity.getId())
                .name(vocabularyEntity.getName())
                .createdOn(vocabularyEntity.getCreatedOn())
                .entryCount(vocabularyEntryRepository.countAllByVocabularyId(vocabularyEntity.getId()))
                .vocabularyEntries(
                        vocabularyEntity.getVocabularyEntries()
                                .stream()
                                .map(vocabularyEntryEntity -> VocabularyEntryDto.builder()
                                        .id(vocabularyEntryEntity.getId())
                                        .targetWord(vocabularyEntryEntity.getTargetWord().getWord())
                                        .build()
                                )
                                .collect(toList())
                )
                .build();
    }

}
