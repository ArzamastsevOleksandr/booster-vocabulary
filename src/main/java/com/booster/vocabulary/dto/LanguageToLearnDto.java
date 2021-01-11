package com.booster.vocabulary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LanguageToLearnDto {
    Long id;
    Timestamp createdOn;
    BaseLanguageDto baseLanguageDto;
    List<VocabularyDto> vocabularyDtos;
}