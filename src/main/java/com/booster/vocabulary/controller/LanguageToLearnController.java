package com.booster.vocabulary.controller;

import com.booster.vocabulary.config.security.UserDetailsImpl;
import com.booster.vocabulary.dto.LanguageToLearnDto;
import com.booster.vocabulary.dto.request.LanguageToLearnRequestDto;
import com.booster.vocabulary.service.LanguageToLearnService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/language-to-learn")
public class LanguageToLearnController {

    private final LanguageToLearnService languageToLearnService;

    @PostMapping(value = "/create", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    ResponseEntity<LanguageToLearnDto> create(@RequestBody LanguageToLearnRequestDto languageToLearnRequestDto) {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        languageToLearnRequestDto.setUserId(userId);
        LanguageToLearnDto languageToLearnDto = languageToLearnService.create(languageToLearnRequestDto);
        return ResponseEntity.ok(languageToLearnDto);
    }

    @GetMapping("/list")
    ResponseEntity<List<LanguageToLearnDto>> findAll() {
        Long userId = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        List<LanguageToLearnDto> languageToLearnDtos = languageToLearnService.findAllByUserId(userId);
        return ResponseEntity.ok(languageToLearnDtos);
    }

    @GetMapping("/{id}")
    ResponseEntity<LanguageToLearnDto> findById(@PathVariable Long id) {
        LanguageToLearnDto languageToLearnDto = languageToLearnService.findById(id);
        return ResponseEntity.ok(languageToLearnDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteById(@PathVariable Long id) {
        languageToLearnService.deleteById(id);
    }

}
