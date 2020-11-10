package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.security.UserDetailsImpl;
import com.booster.vocabulary.entity.LanguageEnum;
import com.booster.vocabulary.service.VocabularyEntryService;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/vocabulary-entry")
public class VocabularyEntryController {

    private final VocabularyEntryService vocabularyEntryService;

    @PostMapping(value = "/add", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<VocabularyEntryResponseId> add(@RequestBody VocabularyEntryRequestDto vocabularyEntryRequestDto) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        vocabularyEntryRequestDto.setUserId(userId);

        Long vocabularyEntryId = vocabularyEntryService.add(vocabularyEntryRequestDto);
        return ResponseEntity.ok(new VocabularyEntryResponseId(vocabularyEntryId));
    }

    @Data
    @Builder
    public static class VocabularyEntryRequestDto {
        String word;
        Set<String> synonyms;
        Set<String> antonyms;

        String vocabularyName;
        LanguageEnum languageName;

        Long userId;
    }

    @Value
    public static class VocabularyEntryResponseId {
        Long vocabularyEntryId;
    }

}
