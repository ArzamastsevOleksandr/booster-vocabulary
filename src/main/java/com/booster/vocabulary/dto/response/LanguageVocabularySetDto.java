package com.booster.vocabulary.dto.response;

import com.booster.vocabulary.dto.LanguageDto;
import com.booster.vocabulary.dto.VocabularyDto;
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
public class LanguageVocabularySetDto {
    Long id;
    Timestamp createdOn;
    LanguageDto languageDto;
    List<VocabularyDto> vocabularyDtoList;
}
