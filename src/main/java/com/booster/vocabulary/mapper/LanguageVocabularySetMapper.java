package com.booster.vocabulary.mapper;

import com.booster.vocabulary.dto.LanguageDto;
import com.booster.vocabulary.dto.VocabularyDto;
import com.booster.vocabulary.dto.LanguageVocabularySetDto;
import com.booster.vocabulary.entity.LanguageEntity;
import com.booster.vocabulary.entity.LanguageVocabularySetEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class LanguageVocabularySetMapper {

    private final LanguageMapper languageMapper;
    private final VocabularyMapper vocabularyMapper;

    public LanguageVocabularySetDto entity2Dto(LanguageVocabularySetEntity languageVocabularySetEntity) {
        LanguageEntity languageEntity = languageVocabularySetEntity.getLanguage();
        LanguageDto languageDto = languageMapper.entity2Dto(languageEntity);
        List<VocabularyDto> vocabularyDtoList = languageVocabularySetEntity.getVocabularies()
                .stream()
                .map(vocabularyMapper::entity2Dto)
                .collect(toList());

        return LanguageVocabularySetDto.builder()
                .id(languageVocabularySetEntity.getId())
                .createdOn(languageVocabularySetEntity.getCreatedOn())
                .languageDto(languageDto)
                .vocabularyDtoList(vocabularyDtoList)
                .build();
    }

}
