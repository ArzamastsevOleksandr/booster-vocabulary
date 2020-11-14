package com.booster.vocabulary.controller;

import com.booster.vocabulary.dto.LanguageDto;
import com.booster.vocabulary.service.LanguageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/language")
public class LanguageController {

    private static final String LIST_PATH = "/list";
    private static final String BY_ID_PATH = "/{languageId}";

    private final LanguageService languageService;

    @GetMapping(LIST_PATH)
    ResponseEntity<List<LanguageDto>> list() {
        log.debug("{}: {}", this.getClass().getName(), LIST_PATH);

        List<LanguageDto> languageDtoList = languageService.findAll();
        return ResponseEntity.ok(languageDtoList);
    }

    @GetMapping(BY_ID_PATH)
    ResponseEntity<LanguageDto> languageById(@PathVariable Long languageId) {
        log.debug("{}: {}. ID: {}", this.getClass().getName(), BY_ID_PATH, languageId);

        LanguageDto languageDto = languageService.findById(languageId);
        return ResponseEntity.ok(languageDto);
    }

}
