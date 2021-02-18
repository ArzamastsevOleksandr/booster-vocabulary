package com.booster.vocabulary.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VocabularyRequestDto {
    String vocabularyName;
    String languageToLearnId;

    @JsonIgnore
    String userId;
}