package com.booster.vocabulary.mapper;

import com.booster.vocabulary.dto.BaseLanguageDto;
import com.booster.vocabulary.dto.VocabularyDto;
import com.booster.vocabulary.dto.LanguageToLearnDto;
import com.booster.vocabulary.entity.BaseLanguageEntity;
import com.booster.vocabulary.entity.LanguageToLearnEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class LanguageToLearnMapper {

    private final BaseLanguageMapper baseLanguageMapper;
    private final VocabularyMapper vocabularyMapper;

    public LanguageToLearnDto entity2Dto(LanguageToLearnEntity languageToLearnEntity) {
        BaseLanguageEntity baseLanguageEntity = languageToLearnEntity.getBaseLanguage();
        BaseLanguageDto baseLanguageDto = baseLanguageMapper.entity2Dto(baseLanguageEntity);
        List<VocabularyDto> vocabularyDtoList = languageToLearnEntity.getVocabularies()
                .stream()
                .map(vocabularyMapper::entity2Dto)
                .collect(toList());

        return LanguageToLearnDto.builder()
                .id(languageToLearnEntity.getId())
                .createdOn(languageToLearnEntity.getCreatedOn())
                .baseLanguageDto(baseLanguageDto)
                .vocabularyDtos(vocabularyDtoList)
                .build();
    }

}
