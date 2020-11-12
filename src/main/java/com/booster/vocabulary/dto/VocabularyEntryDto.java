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
public class VocabularyEntryDto {
    Long id;
    String targetWord;
    Integer correctAnswersCount;
    Timestamp createdOn;
    List<String> antonyms;
    List<String> synonyms;
}