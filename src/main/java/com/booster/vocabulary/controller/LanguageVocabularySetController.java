package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.security.UserDetailsImpl;
import com.booster.vocabulary.dto.LanguageVocabularySetDto;
import com.booster.vocabulary.dto.request.LanguageVocabularySetRequestDto;
import com.booster.vocabulary.service.LanguageVocabularySetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/language-vocabulary-set")
public class LanguageVocabularySetController {

    private final LanguageVocabularySetService languageVocabularySetService;

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<LanguageVocabularySetDto> create(@RequestBody LanguageVocabularySetRequestDto languageVocabularySetRequestDto) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        languageVocabularySetRequestDto.setUserId(userId);
        LanguageVocabularySetDto languageVocabularySetDto = languageVocabularySetService.create(languageVocabularySetRequestDto);
        return ResponseEntity.ok(languageVocabularySetDto);
    }

    @GetMapping("/list")
    ResponseEntity<List<LanguageVocabularySetDto>> list() {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<LanguageVocabularySetDto> languageVocabularySetDtoList = languageVocabularySetService.findAllByUserId(userId);
        return ResponseEntity.ok(languageVocabularySetDtoList);
    }

    @GetMapping("/{id}")
    ResponseEntity<LanguageVocabularySetDto> languageVocabularySetById(@PathVariable Long id) {
        LanguageVocabularySetDto languageVocabularySetDto = languageVocabularySetService.findById(id);
        return ResponseEntity.ok(languageVocabularySetDto);
    }

}
