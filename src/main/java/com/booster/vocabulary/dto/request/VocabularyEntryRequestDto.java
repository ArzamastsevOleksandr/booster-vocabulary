package com.booster.vocabulary.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class VocabularyEntryRequestDto {
    String word;
    Set<String> synonyms;
    Set<String> antonyms;

    Long vocabularyId;

    @JsonIgnore
    Long userId;
}