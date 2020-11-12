package com.booster.vocabulary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyDto {
    Long id;
    String name;
    Integer entryCount;
    Timestamp createdOn;
//    List<VocabularyEntryDto> vocabularyEntries;
}
