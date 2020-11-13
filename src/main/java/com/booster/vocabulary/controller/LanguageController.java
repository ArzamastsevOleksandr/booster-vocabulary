package com.booster.vocabulary.controller;

import com.booster.vocabulary.dto.LanguageDto;
import com.booster.vocabulary.service.LanguageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/language")
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping("/list")
    List<LanguageDto> list() {
        return languageService.findAll();
    }

}
