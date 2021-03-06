package com.booster.vocabulary.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VocabularyEntryDto {
    String id;
    String targetWord;
    Integer correctAnswersCount;
    Timestamp createdOn;
    List<WordDto> antonyms;
    List<WordDto> synonyms;
}
