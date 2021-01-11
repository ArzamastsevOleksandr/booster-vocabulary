package com.booster.vocabulary.mapper;

import com.booster.vocabulary.dto.LanguageDto;
import com.booster.vocabulary.dto.VocabularyDto;
import com.booster.vocabulary.dto.LanguageToLearnDto;
import com.booster.vocabulary.entity.LanguageEntity;
import com.booster.vocabulary.entity.LanguageToLearnEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class LanguageToLearnMapper {

    private final LanguageMapper languageMapper;
    private final VocabularyMapper vocabularyMapper;

    public LanguageToLearnDto entity2Dto(LanguageToLearnEntity languageToLearnEntity) {
        LanguageEntity languageEntity = languageToLearnEntity.getLanguage();
        LanguageDto languageDto = languageMapper.entity2Dto(languageEntity);
        List<VocabularyDto> vocabularyDtoList = languageToLearnEntity.getVocabularies()
                .stream()
                .map(vocabularyMapper::entity2Dto)
                .collect(toList());

        return LanguageToLearnDto.builder()
                .id(languageToLearnEntity.getId())
                .createdOn(languageToLearnEntity.getCreatedOn())
                .languageDto(languageDto)
                .vocabularyDtos(vocabularyDtoList)
                .build();
    }

}
