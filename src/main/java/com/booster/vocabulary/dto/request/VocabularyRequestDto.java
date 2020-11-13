package com.booster.vocabulary.dto.request;

import com.booster.vocabulary.entity.LanguageEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VocabularyRequestDto {
    String vocabularyName;
    LanguageEnum languageName;

    @JsonIgnore
    Long userId;
}