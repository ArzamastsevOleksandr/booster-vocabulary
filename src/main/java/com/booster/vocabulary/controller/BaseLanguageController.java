package com.booster.vocabulary.controller;

import com.booster.vocabulary.dto.BaseLanguageDto;
import com.booster.vocabulary.service.BaseLanguageService;
import lombok.*;
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
@RequestMapping("/base-language")
public class BaseLanguageController {

    private final BaseLanguageService baseLanguageService;

    @GetMapping("/list")
    ResponseEntity<BaseLanguageDtoList> findAll() {
        List<BaseLanguageDto> baseLanguageDtoList = baseLanguageService.findAll();
        return ResponseEntity.ok(new BaseLanguageDtoList(baseLanguageDtoList));
    }

    @GetMapping("/{id}")
    ResponseEntity<BaseLanguageDto> findById(@PathVariable Long id) {
        BaseLanguageDto baseLanguageDto = baseLanguageService.findById(id);
        return ResponseEntity.ok(baseLanguageDto);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BaseLanguageDtoList {
        List<BaseLanguageDto> baseLanguageDtos;
    }

}
