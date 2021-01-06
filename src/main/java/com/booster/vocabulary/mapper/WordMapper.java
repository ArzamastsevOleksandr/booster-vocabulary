package com.booster.vocabulary.mapper;

import com.booster.vocabulary.dto.WordDto;
import com.booster.vocabulary.entity.WordEntity;
import org.springframework.stereotype.Component;

@Component
public class WordMapper {

    public WordDto wordEntity2WordDto(WordEntity wordEntity) {
        return WordDto
                .builder()
                .id(wordEntity.getId())
                .name(wordEntity.getName())
                .build();
    }

}
