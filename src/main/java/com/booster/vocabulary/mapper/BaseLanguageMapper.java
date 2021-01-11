package com.booster.vocabulary.mapper;

import com.booster.vocabulary.dto.BaseLanguageDto;
import com.booster.vocabulary.entity.BaseLanguageEntity;
import org.springframework.stereotype.Component;

@Component
public class BaseLanguageMapper {

    public BaseLanguageDto entity2Dto(BaseLanguageEntity baseLanguageEntity) {
        return BaseLanguageDto
                .builder()
                .id(baseLanguageEntity.getId())
                .name(baseLanguageEntity.getName())
                .build();
    }

}
