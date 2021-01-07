package com.booster.vocabulary.mapper;

import com.booster.vocabulary.dto.LanguageDto;
import com.booster.vocabulary.entity.LanguageEntity;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper {

    public LanguageDto entity2Dto(LanguageEntity languageEntity) {
        return LanguageDto
                .builder()
                .id(languageEntity.getId())
                .name(languageEntity.getName())
                .build();
    }

}