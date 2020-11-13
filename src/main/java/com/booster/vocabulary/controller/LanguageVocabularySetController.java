package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.security.UserDetailsImpl;
import com.booster.vocabulary.dto.request.LanguageVocabularySetRequestDto;
import com.booster.vocabulary.dto.response.LanguageVocabularySetDto;
import com.booster.vocabulary.dto.response.LanguageVocabularySetResponseId;
import com.booster.vocabulary.service.LanguageVocabularySetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/language-vocabulary-set")
public class LanguageVocabularySetController {

    private final LanguageVocabularySetService languageVocabularySetService;

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<LanguageVocabularySetResponseId> create(@RequestBody LanguageVocabularySetRequestDto languageVocabularySetRequestDto) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        languageVocabularySetRequestDto.setUserId(userId);

        Long vocabularyId = languageVocabularySetService.create(languageVocabularySetRequestDto);
        return ResponseEntity.ok(new LanguageVocabularySetResponseId(vocabularyId));
    }

    @GetMapping("/list")
    List<LanguageVocabularySetDto> list() {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return languageVocabularySetService.findAllForUserId(userId);
    }

    @GetMapping("/{languageVocabularySetId}")
    ResponseEntity<LanguageVocabularySetDto> languageVocabularySetById(@PathVariable Long languageVocabularySetId) {
        LanguageVocabularySetDto languageVocabularySetDto = languageVocabularySetService.findById(languageVocabularySetId);
        return ResponseEntity.ok(languageVocabularySetDto);
    }

}
